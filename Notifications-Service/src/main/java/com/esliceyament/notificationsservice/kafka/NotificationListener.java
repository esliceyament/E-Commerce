package com.esliceyament.notificationsservice.kafka;

import com.esliceyament.notificationsservice.service.NotificationService;
import com.esliceyament.shared.payload.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationListener {
    private final NotificationService service;

    @KafkaListener(topics = "notification-events", groupId = "notifications-group")
    public void handleNotificationEvents(NotificationDto dto) {
        service.createNotification(dto.getUsername(), dto.getSubject(), dto.getBody(), dto.getType());
    }
}
