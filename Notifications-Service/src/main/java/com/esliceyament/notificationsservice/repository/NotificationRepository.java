package com.esliceyament.notificationsservice.repository;

import com.esliceyament.notificationsservice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUsernameAndIsReadFalse(String username);
}
