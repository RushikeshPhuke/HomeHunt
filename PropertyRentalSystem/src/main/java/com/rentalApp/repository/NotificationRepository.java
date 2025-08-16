package com.rentalApp.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.rentalApp.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByBrokerIdAndReadStatusFalse(Integer brokerId);
}
