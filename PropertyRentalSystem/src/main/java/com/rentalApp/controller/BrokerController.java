package com.rentalApp.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rentalApp.dto.BrokerDTO;
import com.rentalApp.entity.Broker;
import com.rentalApp.entity.Owner;
import com.rentalApp.services.BrokerService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class BrokerController {

	@Autowired
    private BrokerService brokerService;

	@PostMapping("/addBroker")
    public ResponseEntity<String> addBroker(@RequestBody Broker broker) {
        boolean added = brokerService.addBroker(broker);
        if (added) {
            return ResponseEntity.ok("Broker added successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to add broker. Check input or server logs.");
        }
    }

    @GetMapping("/getAllBrokers")
    public List<Broker> getAllBrokers() {
        return brokerService.getAllBrokers();
    }

    @GetMapping("/{brokerId}/owners")
    public ResponseEntity<List<Owner>> getOwners(@PathVariable Integer brokerId) {
        try {
            List<Owner> owners = brokerService.getOwnersByBrokerId(brokerId);
            return ResponseEntity.ok(owners);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @PostMapping("/broker/login")
    public ResponseEntity<String> login(@RequestBody Broker loginRequest) {
        boolean isValid = brokerService.isValidBroker(
            loginRequest.getEmail(), loginRequest.getPassword()
        );

        if (isValid) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
