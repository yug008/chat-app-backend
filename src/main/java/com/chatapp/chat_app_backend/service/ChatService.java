package com.chatapp.chat_app_backend.service;

import com.chatapp.chat_app_backend.model.ChatMessage;
import com.chatapp.chat_app_backend.model.ChatMessageEntity;
import com.chatapp.chat_app_backend.model.Role;
import com.chatapp.chat_app_backend.model.User;
import com.chatapp.chat_app_backend.repository.ChatMessageRepository;
import com.chatapp.chat_app_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    UserRepository userRepository;

    public ChatMessageEntity saveMessage(ChatMessage chatMessage){               
        ChatMessageEntity entity = new ChatMessageEntity(chatMessage);
        return chatMessageRepository.save(entity);
    }

    public List<ChatMessage> getChatHistory(){                     
                .stream()
                .map(entity->entity.toChatMessage())
                .collect(Collectors.toList());
    }

    public void registerUser(String username){                          
        if(!userRepository.existsByUsername(username)){
            User user = new User();
            user.setUsername(username);
            user.setPassword("temporary");
            user.setRole(Role.USER);
            user.setJoinedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    public List<ChatMessage> getPrivateConversation(String user1, String user2) {               
        return chatMessageRepository.findConversationBetweenUsers(user1, user2)
                .stream()
                .map(entity->entity.toChatMessage())
                .collect(Collectors.toList());
    }

    public List<String> getChatPartners(String username){                  
       return chatMessageRepository.findChatPartners(username);
    }

    public List<String> getAllUsers(){                              
        return userRepository.findAll()
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }
}
