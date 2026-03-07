package com.chatapp.chat_app_backend.repository;

import com.chatapp.chat_app_backend.model.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository                                                                                        
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {            

    List <ChatMessageEntity> findAllByOrderByCreatedAtAsc();                   

    @Query("SELECT e FROM ChatMessageEntity e WHERE " +
            "(e.sender = :user1 AND e.receiver = :user2) OR " +
            "(e.sender = :user2 AND e.receiver = :user1) " +
            "ORDER BY e.createdAt ASC")
    List <ChatMessageEntity> findConversationBetweenUsers(@Param("user1") String user1, @Param("user2") String user2);     

    @Query("SELECT DISTINCT CASE WHEN e.sender = :username THEN e.receiver ELSE e.sender END " +          
            "FROM ChatMessageEntity e " +
            "WHERE (e.sender = :username OR e.receiver = :username) " +
            "AND e.receiver IS NOT NULL")
    List <String> findChatPartners(@Param("username") String username);

}
