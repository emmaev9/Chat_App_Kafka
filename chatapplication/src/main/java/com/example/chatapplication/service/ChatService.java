package com.example.chatapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChatService {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public void sendChat(UUID id, String message) {
        System.out.println("Sent chat message with id " + id + " data: " + message);
        simpMessagingTemplate.convertAndSendToUser(id.toString(), "/topic/chat", message);
    }


}