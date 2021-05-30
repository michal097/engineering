package com.example.demo.chat.kafka;


import com.example.demo.chat.Constants;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import com.example.demo.chat.model.ChatRepository;
import com.example.demo.chat.model.ModelChat;
import com.example.demo.chat.model.PersonalChatPokes;
import com.example.demo.chat.model.PersonalChatPokesRepository;
import com.example.demo.model.Client;
import com.example.demo.mongoRepo.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;

import static java.util.stream.Collectors.toList;


@RestController
@CrossOrigin
@Slf4j
public class ProducerController {


    private final ProducerCallback producerCallback = new ProducerCallback();
    private final ChatRepository chatRepository;
    private final KafkaTemplate<String, ModelChat> kafkaTemplate;
    private final PersonalChatService personalChatService;
    private final ClientRepository clientRepository;
    private final PersonalChatPokesRepository personalChatPokesRepository;
    @Autowired
    public ProducerController(ChatRepository chatRepository,
                              KafkaTemplate<String, ModelChat> kafkaTemplate,
                              PersonalChatService personalChatService,
                              ClientRepository clientRepository,
                              PersonalChatPokesRepository personalChatRepository) {
        this.chatRepository = chatRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.personalChatService = personalChatService;
        this.clientRepository = clientRepository;
        this.personalChatPokesRepository = personalChatRepository;
    }

    @GetMapping("/chat/{message}")
    public String generateMessages(@PathVariable String message) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ModelChat modelChat = new ModelChat(username, message);
        modelChat.setTimeStamp(LocalDateTime.now());
        this.waitFor();
        this.sendToKafka(modelChat);
        chatRepository.save(modelChat);
        return username;
    }

    @GetMapping("getChat")
    public List<ModelChat> getMessages() {
        //group chat, without personals
        return chatRepository.findAll()
                .stream()
                .filter(chat -> chat.getPersonalChat() == null).collect(toList());
    }

    @GetMapping("chat/{username}/{message}")
    public ModelChat personalChat(@PathVariable String username, @PathVariable String message) {
        Map<String, String> chatMap = personalChatService.invitePersonToChat(username);
        String actualUser = "";

        for (Map.Entry<String, String> map : chatMap.entrySet()) {
            actualUser = map.getValue();

        }
        Optional<Client> client = clientRepository.findByUsername(actualUser);
        AtomicReference<String> chatId = new AtomicReference<>();
        client.ifPresent(value -> value.getChatIds().forEach(c -> {
            if (c.contains(username)) {
                chatId.set(c);
            }
        }));
        ModelChat modelChat = new ModelChat(actualUser, message);
        modelChat.setModelChatId(chatId.get());
        modelChat.setTimeStamp(LocalDateTime.now());
        chatRepository.findAll()
                .stream()
                .filter(chat -> chat.getPersonalChat() != null)
                .forEach(c -> {
                    boolean findPersonalChat = c.getPersonalChat().containsKey(chatId.get());
                    if (findPersonalChat) {
                        c.getPersonalChat().get(chatId.get()).add(modelChat);
                        chatRepository.save(c);
                    }
                });
        String finalActualUser = actualUser;
        var pChatPokes = personalChatPokesRepository.findAll()
                .stream()
                .filter(p-> p.getChatID()!= null && p.getChatID()
                        .equals(chatId.get())
                && p.getSendFrom().equals(finalActualUser)).findAny();
        if(!pChatPokes.isPresent()){
            personalChatPokesRepository.save(new PersonalChatPokes(chatId.get(),username, actualUser, 1));
        }else{
            pChatPokes.get().setCounter(pChatPokes.get().getCounter() + 1);
            personalChatPokesRepository.save(pChatPokes.get());
        }

        this.waitFor();
        this.sendToKafka(modelChat);
        return modelChat;
    }

    @GetMapping("getChatCounter/{chatID}/{sendFrom}")
    public int getChatCounter(@PathVariable String chatID, @PathVariable String sendFrom){
        var personalChatTarget = personalChatPokesRepository.findAll()
                .stream()
                .filter(p-> p.getChatID().equals(chatID) && p.getSendFrom().equals(sendFrom))
                .findAny();
        return personalChatTarget.map(PersonalChatPokes::getCounter).orElse(0);
    }

    @GetMapping("markAsRead/{chatID}/{sendFrom}")
    public void markAsRead(@PathVariable String chatID, @PathVariable String sendFrom){
        var personalChatTarget = personalChatPokesRepository.findAll()
                .stream()
                .filter(p-> p.getChatID().equals(chatID) && p.getSendFrom().equals(sendFrom))
                .findAny();
        if(personalChatTarget.isPresent()){
            personalChatTarget.get().setCounter(0);
            personalChatPokesRepository.save(personalChatTarget.get());
        }
    }

    @GetMapping("getSpecChat/{targetUsername}")
    public Map<String, List<ModelChat>> getSpecChat(@PathVariable String targetUsername) {
        String sourceUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String chatId = "";
        Optional<Client> retrieveChatId = clientRepository.findByUsername(sourceUsername);
        if(retrieveChatId.isPresent() && retrieveChatId.get().getChatIds()!= null) {
            for (String chat : retrieveChatId.get().getChatIds()) {
                if (chat.contains(targetUsername)) {
                    chatId = chat;
                }
            }
        }
        String finalChatId = chatId;

        var chatStream = chatRepository.findAll()
                .stream()
                .filter(chat -> chat.getPersonalChat() != null && chat.getPersonalChat().get(finalChatId) != null)
                .findAny();
        return chatStream.map(ModelChat::getPersonalChat).orElse(null);
    }

    @GetMapping("startChatting/{targetUser}")
    public String startChatting(@PathVariable String targetUser) {
        var startChatMap = personalChatService.invitePersonToChat(targetUser);
        String chatID = "";
        for (Map.Entry<String, String> s : startChatMap.entrySet()) {
            chatID = s.getKey();
        }
        return chatID;
    }

    @GetMapping("getMyChats/{user}")
    public Map<String, String> myChats(@PathVariable String user) {
        Map<String, String> map = new HashMap<>();
        Optional<Set<String>> chatIds = clientRepository.findAllByUsername(user)
                .map(Client::getChatIds);

        chatIds.ifPresent(chatIDs -> chatIDs.forEach(c -> map.put(c, c.replaceAll("_", "").replaceAll(user, ""))));
        return map;
    }

    @GetMapping("getAvailableChats")
    public List<String> availableChats() {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        var myChats = clientRepository.findByUsername(user);
        Set<String> chats = new HashSet<>();
        if (myChats.isPresent()) {
            if(myChats.get().getChatIds() != null){
                chats = myChats.get().getChatIds();
            }
        }

        var allClients = clientRepository.findAll().stream().map(Client::getUsername).collect(toList());
        List<String> result = new ArrayList<>();
        for (String u : allClients) {
            if (!chats.toString().contains(u) && !u.equals(user)) {
                result.add(u);
            }
        }
        return result;
    }

    @GetMapping("chat/user")
    public String checkUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private void sendToKafka(ModelChat model) {
        this.kafkaTemplate
                .send(Constants.KAFKA_TOPIC, UUID.randomUUID().toString(), model)
                .addCallback(this.producerCallback);

    }

    private void waitFor() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Future<Void> future = scheduler.schedule(() -> null, 200, TimeUnit.MILLISECONDS);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    static class ProducerCallback implements ListenableFutureCallback<SendResult<String, ModelChat>> {
        @Override
        public void onSuccess(SendResult<String, ModelChat> result) {
            RecordMetadata record = Objects.requireNonNull(result).getRecordMetadata();
            log.info("Sending {} to topic {} - partition {}",
                    result.getProducerRecord().key(),
                    result.getProducerRecord().topic(),
                    record.partition());
        }

        @Override
        public void onFailure(@NotNull Throwable e) {
            log.error("Producer Failure", e);
        }

    }

}