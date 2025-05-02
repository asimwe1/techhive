package com.techhive.Technology.Controllers;

import com.techhive.Technology.Models.ChatSession;
import com.techhive.Technology.Models.Message;
import com.techhive.Technology.Models.User;
import com.techhive.Technology.Repository.ChatSessionRepository;
import com.techhive.Technology.Repository.MessageRepository;
import com.techhive.Technology.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatRestController {
    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/history")
    public ResponseEntity<?> getChatHistory(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }

        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        User user = userOptional.get();
        Optional<ChatSession> sessionOptional = chatSessionRepository.findByUserAndIsActive(user, true);
        if (sessionOptional.isEmpty()) {
            return ResponseEntity.ok(Map.of("sessionId", null, "messages", List.of()));
        }

        ChatSession chatSession = sessionOptional.get();
        List<Message> messages = messageRepository.findByChatSession(chatSession);
        List<Map<String, ? extends Serializable>> messageDTOs = new ArrayList<>();
        
        for (Message message : messages) {
            Map<String, Serializable> messageDTO = new HashMap<>();
            messageDTO.put("id", message.getId());
            messageDTO.put("sender", message.getSender() != null ? message.getSender() : "Unknown");
            messageDTO.put("text", message.getContent() != null ? message.getContent() : "");
            messageDTO.put("time", message.getTimestamp() != null ? message.getTimestamp().toString() : "");
            messageDTO.put("chatSessionId", chatSession.getId());
            messageDTOs.add(messageDTO);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", chatSession.getId());
        response.put("messages", messageDTOs);

        return ResponseEntity.ok(response);
    }
}
