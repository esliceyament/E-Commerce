package com.esliceyament.notificationsservice.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {

    private String subject;
    private String body;
    private String type;

    private LocalDateTime timeStamp;
}
