package com.Project.ChatApp.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private String senderName;
    private String receiverName;
    private String message;
    private Date date;
    private Status status;
}
