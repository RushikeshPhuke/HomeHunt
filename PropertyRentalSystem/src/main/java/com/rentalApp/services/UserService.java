package com.rentalApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rentalApp.entity.Notification;
import com.rentalApp.entity.User;
import com.rentalApp.repository.NotificationRepository;
import com.rentalApp.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private NotificationRepository notificationRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        // ✅ Encrypt password before saving
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        user.setRegistrationDate(java.time.LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // ✅ Send notification with user details if broker is selected
        if (user.getBroker() != null && user.getBroker().getBrokerId() != null) {
            Notification notification = new Notification(
                    user.getBroker().getBrokerId(),
                    "You have been selected by a new user during registration!",
                    user.getName(),        // Store user's name
                    user.getPhone()  // Store user's phone number
            );
            notificationRepository.save(notification);
        }

        return savedUser;
    }

    public List<User> getUsersByBroker(Integer brokerId) {
        return userRepository.findByBroker_BrokerId(brokerId);
    }

    public boolean isValidUser(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return passwordEncoder.matches(rawPassword, user.getPassword());
        }
        return false;
    }
}
