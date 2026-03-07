//This "ChatMessageEntity.java" class is for DATABASE STORAGE (saving messages permanently). Purpose:Persistence.(Saved permanently)
package com.chatapp.chat_app_backend.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = true)
    private String receiver;

    @Column(nullable = true, length = 1000)                               
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatMessage.MessageType type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // No-args constructor
    public ChatMessageEntity() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructor from ChatMessage
    public ChatMessageEntity(ChatMessage chatMessage) {
        this.sender = chatMessage.getSender();
        this.receiver = chatMessage.getReceiver();
        this.content = chatMessage.getContent();
        this.type = chatMessage.getType();
        this.createdAt = LocalDateTime.now();
    }

    // Convert to ChatMessage (for sending to frontend)
    public ChatMessage toChatMessage() {
        ChatMessage message = new ChatMessage();
        message.setSender(this.sender);
        message.setReceiver(this.receiver);
        message.setContent(this.content);
        message.setType(this.type);
        message.setTimestamp(this.createdAt.toString());
        return message;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ChatMessage.MessageType getType() {
        return type;
    }

    public void setType(ChatMessage.MessageType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
