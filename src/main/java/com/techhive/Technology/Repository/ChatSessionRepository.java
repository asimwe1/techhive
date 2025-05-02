package com.techhive.Technology.Repository;

import com.techhive.Technology.Models.ChatSession;
import com.techhive.Technology.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    Optional<ChatSession> findByUserAndIsActive(User user, boolean isActive);
}
