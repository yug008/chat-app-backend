//This 'ChatMessage.java' class is for WEBSOCKET COMMUNICATION (sending/receiving messages in real-time).Purpose:Data transfer over network.(No database stuff)
package com.chatapp.chat_app_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
public class ChatMessage {

    private String sender;                         
    private String receiver;
    private String content;                        
    private ChatMessage.MessageType type;              
    private String timestamp;                       
    private boolean isTyping;                       
    private boolean read;                           

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public enum MessageType{
        CHAT, JOIN, LEAVE, TYPING, READ
    }


    // Constructors
    public ChatMessage() {}

    public ChatMessage(String sender, String content, MessageType type, String timestamp) {
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.timestamp = timestamp;
    }

    // Getters
    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public MessageType getType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
