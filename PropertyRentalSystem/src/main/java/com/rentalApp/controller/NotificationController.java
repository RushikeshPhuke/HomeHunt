package com.rentalApp.controller;


import com.rentalApp.entity.Notification;
import com.rentalApp.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    // Get all unread notifications for broker
    @GetMapping("/notifications/{brokerId}")
    public List<Notification> getBrokerNotifications(@PathVariable Integer brokerId) {
        return notificationRepository.findByBrokerIdAndReadStatusFalse(brokerId);
    }

    // Mark notification as read
    @PutMapping("/notifications/{id}/read")
    public void markAsRead(@PathVariable Integer id) {
        Notification notification = notificationRepository.findById(id).orElseThrow();
        notification.setReadStatus(true);
        notificationRepository.save(notification);
    }
}
