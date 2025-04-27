// config/WebSocketConfig.java
package com.techhive.Technology.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Enable simple in-memory message broker
        config.setApplicationDestinationPrefixes("/app"); // Prefix for client messages
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS();
    }

//    @Bean
//    public SimpMessagingTemplate messagingTemplate() {
//        SimpMessagingTemplate simpMessagingTemplate = new SimpMessagingTemplate(messageBroker());
//        return simpMessagingTemplate;
//    }
//
//    @Bean
//    public org.springframework.messaging.simp.SimpMessageSendingOperations messageBroker() {
//        return new org.springframework.messaging.simp.SimpMessagingTemplate(
//                new org.springframework.messaging.support.ExecutorSubscribableChannel()
//        );
//    }
}