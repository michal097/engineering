package com.example.demo.chat.kafka;


import com.example.demo.chat.Constants;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.example.demo.chat.model.ChatRepository;
import com.example.demo.chat.model.ModelChat;
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

    @Autowired
    public ProducerController(ChatRepository chatRepository,
                              KafkaTemplate<String, ModelChat> kafkaTemplate,
                              PersonalChatService personalChatService,
                              ClientRepository clientRepository) {
        this.chatRepository = chatRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.personalChatService = personalChatService;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/chat/{message}")
    public String generateMessages(@PathVariable String message) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ModelChat modelChat = new ModelChat(username, message);
        this.waitFor();
        this.sendToKafka(modelChat);
        chatRepository.save(modelChat);
        return username;
    }

    @GetMapping("getChat")
    public List<ModelChat> getMessages() {
        //group chat, without personals
        return chatRepository.findAll().stream().filter(chat -> chat.getPersonalChat() == null).collect(toList());
    }

    @GetMapping("chat/{username}/{message}")
    public ModelChat personalChat(@PathVariable String username, @PathVariable String message) {
        Map<String, String> chatMap = personalChatService.invitePersonToChat(username);
        String actualUser = "";

        for (Map.Entry<String, String> map : chatMap.entrySet()) {
            actualUser = map.getValue();

        }
        //todo change to source user, for test its target username because source is anonymous
        Optional<Client> client = clientRepository.findByUsername(username);
        AtomicReference<String> chatId = new AtomicReference<>();
        client.ifPresent(value -> value.getChatIds().forEach(c -> {
            if (c.contains(username)) {
                chatId.set(c);
            }
        }));
        ModelChat modelChat = new ModelChat(actualUser, message);
        modelChat.setModelChatId(chatId.get());
        chatRepository.findAll()
                .stream()
                .filter(chat -> chat.getPersonalChat() != null)
                .forEach(c -> {
                    System.out.println(chatId);
                    c.getPersonalChat().get(chatId.get()).add(modelChat);
                    chatRepository.save(c);
                });

        this.waitFor();
        System.out.println("model chat personal : " + modelChat);
        this.sendToKafka(modelChat);
        return modelChat;
    }

    @GetMapping("getSpecChat/{targetUsername}")
    public List<String> getSpecChat(@PathVariable String targetUsername) {
        String sourceUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String chatId = "";
        //todo as above change to source username
        var retrieveChatId = clientRepository.findByUsername(targetUsername).get().getChatIds();
        for (String chat : retrieveChatId) {
            if (chat.contains(targetUsername)) {
                chatId = chat;
            }
        }
        String finalChatId = chatId;
        return chatRepository.findAll()
                .stream()
                .map(pChat -> pChat.getPersonalChat().get(finalChatId))
                .flatMap(Collection::stream)
                .map(ModelChat::getMessage)
                .collect(toList());

    }

    @GetMapping("getMyChats/{user}")
    public List<String> myChats(@PathVariable String user) {
        Optional<Client> myChat = clientRepository.findAllByUsername(user);
        return myChat.map(client -> client
                .getChatIds()
                .stream()
                .map(chat -> chat.replaceAll("_", "").replaceAll(user, ""))
                .collect(toList())).orElse(null);
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