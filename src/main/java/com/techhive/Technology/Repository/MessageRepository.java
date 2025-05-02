package com.techhive.Technology.Repository;

import com.techhive.Technology.Models.ChatSession;
import com.techhive.Technology.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatSession(ChatSession chatSession);
}
