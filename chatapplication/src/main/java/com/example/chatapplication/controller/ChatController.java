package com.example.chatapplication.controller;

import com.example.chatapplication.service.ChatService;
import com.example.chatapplication.websocket.SessionStorage;
import com.example.chatapplication.websocket.WebSocketConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Controller
@CrossOrigin(origins = "http://localhost:8080")
public class ChatController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    ChatService chatService;

    @Autowired
    SessionStorage sessionStorage;

    @Autowired
    WebSocketConfig webSocketConfig;

    @MessageMapping("/sendGroupMessage")
    public void sendGroupMessage(String message) {
        String groupName = message.split("-")[2];

        kafkaTemplate.send("group", groupName, message);
        System.out.println("Sent group in kafka message " + message);
    }

    @MessageMapping("/sendPrivateMessage")
    public void sendPrivateMessage(String message) {
        System.out.println("Sent in kafka message " + message);
        String sender = message.split("-")[1];
        String destination = message.split("-")[2];
        String kafkaKey = "";
        if(sender.compareTo(destination) < 0) {
            kafkaKey = sender + "-" + destination;
        } else {
            kafkaKey = destination + "-" + sender;
        }
        kafkaTemplate.send("private", kafkaKey, message);
    }

    @PostMapping(value = "/sendPrivate")
    public ResponseEntity<?> sendPrivate(@RequestBody String message) {
        System.out.println("MESSAGE RECEIVED: " + message);

        kafkaTemplate.send("private", message.split("/")[0], message);
        System.out.println("Sent in kafka message " + message);
        return ResponseEntity.ok("Succes");
    }

    @PostMapping(value = "/sendGroup")
    public ResponseEntity<?> sendGroup(@RequestBody String message) {
        System.out.println("MESSAGE RECEIVED: " + message);

        kafkaTemplate.send("group", message.split("/")[0], message);
        System.out.println("Sent in kafka message " + message);
        return ResponseEntity.ok("Succes");
    }
}
