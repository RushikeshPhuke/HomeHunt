package com.rentalApp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.rentalApp.entity.Booking;
import com.rentalApp.entity.Property;
import com.rentalApp.entity.User;
import com.rentalApp.repository.BookingRepository;
import com.rentalApp.repository.PropertyRepository;
import com.rentalApp.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PropertyRepository propertyRepo;

   
    @PostMapping("/send")
    public ResponseEntity<String> sendInquiry(
            @RequestParam int userId,
            @RequestParam int propertyId,
            @RequestBody String message) {

        Optional<User> userOpt = userRepo.findById(userId);
        Optional<Property> propertyOpt = propertyRepo.findById(propertyId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (propertyOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
        }

        Booking booking = new Booking();
        booking.setUser(userOpt.get());
        booking.setProperty(propertyOpt.get());
        booking.setMessage(message);
        booking.setStatus("Pending");
        booking.setInquiryDate(LocalDateTime.now());

        bookingRepo.save(booking);

        return ResponseEntity.ok("Inquiry sent successfully.");
    }
}

