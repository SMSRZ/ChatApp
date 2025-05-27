package com.Project.ChatApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocketConfig configures the WebSocket message broker for handling
 * STOMP messages in the ChatApp application.
 */
@Configuration
@EnableWebSocketMessageBroker // Enables WebSocket message handling, backed by a message broker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configure the message broker.
     *
     * @param registry the MessageBrokerRegistry to configure broker settings
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Prefix for messages bound for methods annotated with @MessageMapping
        registry.setApplicationDestinationPrefixes("/app");

        // Enable simple in-memory broker for destinations prefixed with /chatroom and /user
        registry.enableSimpleBroker("/chatroom", "/user");

        // Prefix used for user-specific messages (e.g., private chat)
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * Register STOMP endpoints for WebSocket communication.
     *
     * @param registry the StompEndpointRegistry to register WebSocket endpoints
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register an endpoint at /ws for WebSocket handshake
        // Allows connections from all origins (use caution in production)
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // Fallback support for browsers that don't support WebSocket
    }
}
