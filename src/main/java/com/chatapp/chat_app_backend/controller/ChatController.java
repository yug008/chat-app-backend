package com.chatapp.chat_app_backend.controller;

import com.chatapp.chat_app_backend.model.ChatMessage;
import com.chatapp.chat_app_backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    ChatService chatService;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        chatService.saveMessage(chatMessage);            //save to database
        System.out.println("Message saved: " + chatMessage.getContent() + " from " + chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headAccessor){
        headAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        chatService.registerUser(chatMessage.getSender());          //register user in database
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        chatService.saveMessage(chatMessage);                 //Save JOIN message to database


        System.out.println("User registered and joined: " + chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/chat.sendPrivateMessage")
    public void sendPrivateMessage(@Payload ChatMessage chatMessage, Principal principal) {
        System.out.println("Principal: " + (principal != null ? principal.getName() : "NULL"));
        System.out.println("Sender: " + chatMessage.getSender());
        System.out.println("Receiver: " + chatMessage.getReceiver());

        chatMessage.setTimestamp(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        );

        messagingTemplate.convertAndSendToUser(                //send to receiver
                chatMessage.getReceiver(),
                "/queue/private",
                chatMessage
        );

        messagingTemplate.convertAndSendToUser(               // Send back to sender so they see their own message
                chatMessage.getSender(),
                "/queue/private",
                chatMessage
        );

        // Save to database
        chatService.saveMessage(chatMessage);

        System.out.println("Private message from " + chatMessage.getSender() +
                " to " + chatMessage.getReceiver() + ": " + chatMessage.getContent());
    }

    //Typing indicator
    @MessageMapping("/chat.typing")
    public void handleTyping(@Payload ChatMessage chatMessage, Principal principal){
        chatMessage.setType(ChatMessage.MessageType.TYPING);

        if(chatMessage.getReceiver() != null){                         //for private typing indicator
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getReceiver(),
                    "/queue/private",
                    chatMessage
            );
        }
        else{                                                         //for public typing indicator
            messagingTemplate.convertAndSend(
                    "/topic/public",
                    chatMessage
            );
        }
    }

    //Read receipts
    @MessageMapping("/chat.read")
    public void handleRead(@Payload ChatMessage chatMessage, Principal principal){
        chatMessage.setType(ChatMessage.MessageType.READ);

        messagingTemplate.convertAndSendToUser(                     //Notify the original sender that their message was read
                chatMessage.getReceiver(),
                "/queue/private",
                chatMessage
        );
    }

    @org.springframework.web.bind.annotation.GetMapping("/api/users")
    @org.springframework.web.bind.annotation.ResponseBody
    public List<String> getAllUsers(){                                   //calls the service method to get a list of all users
        return chatService.getAllUsers();
    }

    @org.springframework.web.bind.annotation.GetMapping("/api/conversation")
    @org.springframework.web.bind.annotation.ResponseBody
    public List<ChatMessage> getConversation(
            @org.springframework.web.bind.annotation.RequestParam String user1,
            @org.springframework.web.bind.annotation.RequestParam String user2
    ) {
        return chatService.getPrivateConversation(user1, user2);
    }

    @org.springframework.web.bind.annotation.GetMapping("/api/history")
    @org.springframework.web.bind.annotation.ResponseBody
    public List<ChatMessage> getChatHistory() {
        return chatService.getChatHistory();
    }

    @GetMapping("/health")
public ResponseEntity<String> health() {
    return ResponseEntity.ok("UP");
}

}


