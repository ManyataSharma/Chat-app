package com.chat.app.controller;

import com.chat.app.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class chatController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public chatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(ChatMessage message ){
        // Stamp server-side time if missing
        if (message.getTimestamp() == null) {
            message.setTimestamp(System.currentTimeMillis());
        }

        // Deliver to receiver's personal topic
        if (message.getReceiver() != null && !message.getReceiver().isEmpty()) {
            messagingTemplate.convertAndSend("/topic/user." + message.getReceiver(), message);
        }

        // Echo to sender's personal topic (so sender sees their own message update)
        if (message.getSender() != null && !message.getSender().isEmpty()) {
            messagingTemplate.convertAndSend("/topic/user." + message.getSender(), message);
        }
    }
    @GetMapping("chat")
    public String chat(){
        return "chat";
    }
}
