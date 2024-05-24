package com.example.chatapplication.service;

import com.example.chatapplication.model.Group;
import com.example.chatapplication.websocket.SessionStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.UUID;

@Service
public class KafkaConsumer {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    SessionStorage sessionStorage;
    @Autowired
    GroupService groupService;


    @KafkaListener(topics = "private", groupId = "chat-group")
    public void listenPrivate(String message) {
        System.out.println("Consumer private received " + message);
        UUID sessionID = sessionStorage.getSessionID(message.split("-")[1]);


    }

    @KafkaListener(topics = "group", groupId = "chat-group")
    public void listenGroup(String message) {
        System.out.println("Consumer group received " + message);

        String groupName = message.split("-")[2];
        Group group = groupService.getGroup(groupName);
        Iterator<String> iter = group.getUsernames().iterator();
        while(iter.hasNext()) {
            String username = iter.next();
            UUID sessionID = sessionStorage.getSessionID(username);
            if(sessionID != null) {
                simpMessagingTemplate.convertAndSendToUser(sessionID.toString(), "/topic/chat", message);
            }
        }
    }

}
