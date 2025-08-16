package com.rentalApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rentalApp.entity.Broker;
import com.rentalApp.entity.Owner;
import com.rentalApp.entity.Property;
import com.rentalApp.entity.Notification;
import com.rentalApp.repository.NotificationRepository;
import com.rentalApp.services.BrokerService;
import com.rentalApp.services.OwnerService;
import com.rentalApp.services.PropertyService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class OwnerController {

    @Autowired
    OwnerService ownerService;

    @Autowired
    PropertyService propertyService;

    @Autowired
    private BrokerService brokerService;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping("/addOwner")
    public ResponseEntity<String> addOwner(@RequestBody Owner owner) {
        boolean success = ownerService.addOwner(owner);
        if (success) {
            return ResponseEntity.ok("Owner added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to add owner. Please check input or server logs.");
        }
    }

    @PostMapping("/addProperty")
    public ResponseEntity<?> addProperty(@RequestBody Property property) {
        try {
            Owner owner = property.getOwner();

            if (owner != null && owner.getBrokerId() != null) {
                Integer brokerId = owner.getBrokerId();
                Broker broker = brokerService.getBrokerById(brokerId);
                if (broker != null) {
                    property.setBroker(broker);

                    // ✅ Send notification with owner details
                    Notification notification = new Notification(
                            brokerId,
                            "You have been selected by an owner for a property!",
                            owner.getName(),        // Owner name
                            owner.getPhone()  // Owner phone
                    );
                    notificationRepository.save(notification);
                }
            }

            propertyService.addProperty(property);
            return ResponseEntity.ok("Property added successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error adding property: " + e.getMessage());
        }
    }


    @GetMapping("/broker/notifications/{brokerId}")
    public ResponseEntity<?> getBrokerNotifications(@PathVariable int brokerId) {
        return ResponseEntity.ok(notificationRepository.findByBrokerIdAndReadStatusFalse(brokerId));
    }
}
