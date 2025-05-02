// Controllers/ChatController.java
package com.techhive.Technology.Controllers;

import com.techhive.Technology.Models.ChatSession;
import com.techhive.Technology.Models.Message;
import com.techhive.Technology.Models.User;
import com.techhive.Technology.Repository.ChatSessionRepository;
import com.techhive.Technology.Repository.MessageRepository;
import com.techhive.Technology.Repository.UserRepository;
import com.techhive.Technology.config.JwtUtil;
import com.techhive.Technology.DTO.ChatMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO messageDTO, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("Unauthenticated chat message attempt");
            return;
        }

        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            System.out.println("User not found: " + username);
            return;
        }

        User user = userOptional.get();
        Optional<ChatSession> sessionOptional = chatSessionRepository.findByUserAndIsActive(user, true);
        ChatSession chatSession;
        if (sessionOptional.isEmpty()) {
            chatSession = new ChatSession();
            chatSession.setUser(user);
            chatSession.setStartTime(LocalDateTime.now());
            chatSession.setActive(true);
            chatSession = chatSessionRepository.save(chatSession);
            System.out.println("Created new chat session for user: " + username);
        } else {
            chatSession = sessionOptional.get();
        }

        // Save user message
        Message userMessage = new Message();
        userMessage.setChatSession(chatSession);
        userMessage.setSender("user");
        userMessage.setContent(messageDTO.getContent());
        userMessage.setTimestamp(LocalDateTime.now());
        messageRepository.save(userMessage);

        // Broadcast user message
        ChatMessageDTO userMessageDTO = new ChatMessageDTO();
        userMessageDTO.setSender("user");
        userMessageDTO.setContent(messageDTO.getContent());
        userMessageDTO.setTime("Just now");
        messagingTemplate.convertAndSend("/topic/chat/" + chatSession.getId(), userMessageDTO);

        // Simulate support response
        Message supportMessage = new Message();
        supportMessage.setChatSession(chatSession);
        supportMessage.setSender("support");
        supportMessage.setContent("Thank you for your message. Our team will get back to you shortly.");
        supportMessage.setTimestamp(LocalDateTime.now());
        messageRepository.save(supportMessage);

        ChatMessageDTO supportMessageDTO = new ChatMessageDTO();
        supportMessageDTO.setSender("support");
        supportMessageDTO.setContent(supportMessage.getContent());
        supportMessageDTO.setTime("Just now");
        messagingTemplate.convertAndSend("/topic/chat/" + chatSession.getId(), supportMessageDTO);
    }
}

