package com.example.demo.chat.kafka;

import com.example.demo.chat.model.ChatRepository;
import com.example.demo.chat.model.ModelChat;
import com.example.demo.mongoRepo.ClientRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PersonalChatService {

    private final ClientRepository clientRepository;
    private final ChatRepository chatRepository;

    public PersonalChatService(ClientRepository clientRepository, ChatRepository chatRepository) {
        this.clientRepository = clientRepository;
        this.chatRepository = chatRepository;
    }

    public Map<String, String> invitePersonToChat(String personUsername) {
        String whoIAm = SecurityContextHolder.getContext().getAuthentication().getName();
        String chatID = whoIAm + "_" + personUsername;

        clientRepository.findAll().stream().filter(u -> u.getUsername().equals(whoIAm) || u.getUsername().equals(personUsername)).forEach(chat -> {
            if (chat.getChatIds() == null) {
                chat.setChatIds(Set.of(chatID));
            } else {
                chat.getChatIds().add(chatID);
            }
            clientRepository.save(chat);
        });
        boolean initiate = chatRepository.findAll().stream().anyMatch(c-> c.getPersonalChat()!= null && c.getPersonalChat().get(chatID) != null);
        Map<String, List<ModelChat>> initiateNewChat;
        if(!initiate) {
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
