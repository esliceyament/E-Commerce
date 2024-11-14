package com.esliceyament.notificationsservice.controller;

import com.esliceyament.notificationsservice.response.NotificationResponse;
import com.esliceyament.notificationsservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService service;

    @PostMapping("/create")
    public NotificationResponse createNotification(
            @RequestParam String username,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam String type) {
        return service.createNotification(username, subject, body, type);
    }

    @GetMapping("/user/{username}")
    public List<NotificationResponse> getNotifications(@PathVariable String username) {
        return service.getNotificationsForUser(username);
    }

    @PutMapping("/{notificationId}/read")
    public void markAsRead(@PathVariable Long notificationId) {
        service.markAsRead(notificationId);
    }
}
