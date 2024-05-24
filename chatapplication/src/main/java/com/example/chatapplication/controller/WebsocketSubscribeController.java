package com.example.chatapplication.controller;

import com.example.chatapplication.websocket.SessionStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
public class WebsocketSubscribeController {

    @Autowired
    SessionStorage sessionStorage;

    @MessageMapping("/subscribe")
    public void subscribe(String username, Principal principal) {
        System.out.println("Mapped" + username +  " to " + principal.getName());
        sessionStorage.addPair(UUID.fromString(principal.getName()), username);
    }

    @MessageMapping("/unsubscribe")
    public void unsubscribe(Principal principal) {
        sessionStorage.deletePair(UUID.fromString(principal.getName()));
    }


}
