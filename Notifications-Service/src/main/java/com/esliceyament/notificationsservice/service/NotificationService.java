package com.esliceyament.notificationsservice.service;

import com.esliceyament.notificationsservice.entity.Notification;
import com.esliceyament.notificationsservice.mapper.NotificationMapper;
import com.esliceyament.notificationsservice.repository.NotificationRepository;
import com.esliceyament.notificationsservice.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    @Transactional
    public NotificationResponse createNotification(String username, String subject, String body, String type) {
        Notification notification = new Notification();
        notification.setUsername(username);
        notification.setSubject(subject);
        notification.setBody(body);
        notification.setType(type);
        notification.setSent(true);
        notification.setIsRead(false);
        notification.setTimeStamp(LocalDateTime.now());

        repository.save(notification);
        NotificationResponse response = new NotificationResponse();
        response.setSubject(subject);
        response.setBody(body);
        response.setType(type);
        response.setTimeStamp(notification.getTimeStamp());
        return response;
    }

    public List<NotificationResponse> getNotificationsForUser(String username) {
        return repository.findByUsernameAndIsReadFalse(username).stream()
                .map(mapper::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = repository.findById(notificationId).orElseThrow();
        notification.setIsRead(true);
        repository.save(notification);
    }
}
