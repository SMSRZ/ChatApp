package com.Project.ChatApp.Controller;

import com.Project.ChatApp.Model.Message;
import com.Project.ChatApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * ChatController handles incoming WebSocket messages and routes them to appropriate destinations.
 * It manages both public and private messaging functionalities in a chat application.
 */
@Controller
public class ChatController {

    /**
     * SimpMessagingTemplate is used to send messages to specific users.
     * It's a helper class provided by Spring to simplify messaging to clients via WebSocket.
     */
    @Autowired
    private SimpMessagingTemplate simpmsgtemplate;
    @Autowired
    private UserService userService;
    /**
     * Handles messages sent to the "/app/message" destination (from the client).
     * Broadcasts the received message to all subscribers of "/chatroom/public".
     *
     * @param message the message payload received from a user
     * @return the same message object, which will be sent to all public chatroom subscribers
     */
    @MessageMapping("/message") // Listens to messages sent to /app/message
    @SendTo("/chatroom/public") // Forwards the message to all subscribers of /chatroom/public
    private Message receivePublicMessage(@Payload Message message) {
        return message;
    }

    /**
     * Handles private messages sent to the "/app/private-message" destination.
     * Sends the message to a specific user identified by the receiver's username.
     *
     * @param message the message payload containing sender and receiver info
     * @return the same message object, which may be used for acknowledgment or UI updates
     */
    @MessageMapping("/private-message") // Listens to messages sent to /app/private-message
    private Message receivePrivateMessage(@Payload Message message) {
        // Sends the message to the specific user at the destination "/user/{receiverName}/private"
        simpmsgtemplate.convertAndSendToUser(
                message.getReceiverName(), // Receiver's username
                "/private",                // Destination suffix
                message                    // Message payload
        );
        return message;
    }

    @MessageMapping("/adduser")
    @SendTo("/chatroom/public")
    public Message addUser(@Payload Message msg , SimpMessageHeaderAccessor accessor){
        accessor.getSessionAttributes().put("username",msg.getSenderName());
      userService.addUser(msg.getSenderName());
        System.out.println("user list" + userService.getUsers());
      simpmsgtemplate.convertAndSendToUser(
              msg.getSenderName(),"/active-users",userService.getUsers()
      );
        return msg;
    }
}
