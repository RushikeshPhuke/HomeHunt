package com.rentalApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentalApp.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer> {}
