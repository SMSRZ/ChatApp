package com.Project.ChatApp.config;

import com.Project.ChatApp.Model.Message;
import com.Project.ChatApp.Model.Status;
import com.Project.ChatApp.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessageSendingOperations msgtmp;
    @Autowired
    private UserService userservice;
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String)accessor.getSessionAttributes().get("username");
        if(username!=null){
            log.info("User Disconnected : {}",username);
            var chatMessage = Message.builder()
                    .status(Status.LEAVE)
                    .senderName(username)
                    .build();
            System.out.println(chatMessage);
            msgtmp.convertAndSend("/chatroom/public",chatMessage);
            userservice.removeUser(username);
            System.out.println("user list after remove" + userservice.getUsers());
        }

    }
}
