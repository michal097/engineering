package com.example.demo.chat.kafka;

import com.example.demo.chat.model.ChatRepository;
import com.example.demo.chat.model.ModelChat;
import com.example.demo.mongoRepo.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class PersonalChatService {

    private final ClientRepository clientRepository;
    private final ChatRepository chatRepository;

    public PersonalChatService(ClientRepository clientRepository, ChatRepository chatRepository) {
        this.clientRepository = clientRepository;
        this.chatRepository = chatRepository;
    }

    @Transactional
    public Map<String, String> invitePersonToChat(String personUsername) {
        String whoIAm = SecurityContextHolder.getContext().getAuthentication().getName();
        String chatID = whoIAm + "_" + personUsername;

        clientRepository.findAll()
                .stream()
                .filter(u -> u.getUsername().equals(whoIAm)
                        || u.getUsername().equals(personUsername)) //fetch actual user and target user
                .forEach(chat -> {
                    if (chat.getChatIds() == null || chat.getChatIds().isEmpty()) {
                        chat.setChatIds(Set.of(chatID));
                        log.info("Chat id's is empty, create new set");
                    } else {
                        //check two ways of chat relation
                        if (!chat.getChatIds().toString().contains(personUsername)
                                || !chat.getChatIds().toString().contains(whoIAm)) {
                            log.info("Chat id's don't contains such person, creates new chatID");
                            chat.getChatIds().add(chatID);
                        }
                    }
                    clientRepository.save(chat);
                });
        boolean initiate = chatRepository.findAll()
                .stream()
                .anyMatch(c -> c.getPersonalChat() != null
                        && c.getPersonalChat().get(chatID) != null);
        Map<String, List<ModelChat>> initiateNewChat;
        if (!initiate) {
            initiateNewChat = new HashMap<>();
            initiateNewChat.put(chatID, new ArrayList<>());
            ModelChat m = new ModelChat();
            m.setPersonalChat(initiateNewChat);
            chatRepository.save(m);
        }

        Map<String, String> map = new HashMap<>();
        map.put(chatID, whoIAm);
        return map;
    }
}
