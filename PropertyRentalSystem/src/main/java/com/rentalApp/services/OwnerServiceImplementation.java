package com.rentalApp.services;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rentalApp.entity.Broker;
import com.rentalApp.entity.Notification;
import com.rentalApp.entity.Owner;
import com.rentalApp.repository.BrokerRepository;
import com.rentalApp.repository.NotificationRepository;
import com.rentalApp.repository.OwnerRepository;

@Service
public class OwnerServiceImplementation implements OwnerService {

    @Autowired
    OwnerRepository OwnerRepo;

    @Autowired
    BrokerRepository BrokerRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public boolean addOwner(Owner owner) {
        try {
            // ✅ Validate broker if selected
            if (owner.getBrokerId() != null && owner.getBrokerId() != 0) {
                Broker broker = BrokerRepo.findById(owner.getBrokerId()).orElse(null);
                if (broker == null) {
                    System.out.println("Broker not found with ID: " + owner.getBrokerId());
                    return false;
                }
            } else {
                owner.setBrokerId(null);
            }

            // ✅ Encrypt password
            if (owner.getPassword() != null && !owner.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(owner.getPassword());
                owner.setPassword(encodedPassword);
            }

            owner.setRegistrationDate(LocalDate.now());
            OwnerRepo.save(owner);

            // ✅ Send notification if broker is selected
            if (owner.getBrokerId() != null) {
                Notification notification = new Notification(
                        owner.getBrokerId(),
                        "You have been selected by a new owner during registration!",
                        owner.getName(),          // Store Owner Name
                        owner.getPhone()    // Store Owner Phone
                );
                notificationRepository.save(notification);
            }

            System.out.println("Owner saved: " + owner);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Owner getOwnerByEmailAndPassword(String email, String password) {
        return OwnerRepo.findByEmailAndPassword(email, password);
    }

    public boolean isValidOwner(String email, String rawPassword) {
        Optional<Owner> ownerOpt = OwnerRepo.findByEmail(email);
        if (ownerOpt.isPresent()) {
            Owner owner = ownerOpt.get();
            return passwordEncoder.matches(rawPassword, owner.getPassword());
        }
        return false;
    }
}
