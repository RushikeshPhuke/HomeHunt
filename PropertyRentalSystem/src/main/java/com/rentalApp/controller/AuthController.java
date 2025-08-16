package com.rentalApp.controller;

import com.rentalApp.security.JwtUtil;
import com.rentalApp.services.BrokerService;
import com.rentalApp.services.CustomUserDetailsService;
import com.rentalApp.services.OwnerService;
import com.rentalApp.services.UserService;
import com.rentalApp.entity.Broker;
import com.rentalApp.entity.Owner;
import com.rentalApp.entity.User;
import com.rentalApp.repository.BrokerRepository;
import com.rentalApp.repository.OwnerRepository;
import com.rentalApp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private BrokerRepository brokerRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private OwnerService ownerService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BrokerService brokerService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Map<String, Object> response = new HashMap<>();

        // 1. Check User
        if (userService.isValidUser(email, password)) {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                List<String> roles = List.of("ROLE_USER");
                String token = jwtUtil.generateToken(email, roles);

                response.put("message", "Login successful!");
                response.put("token", token);
                response.put("role", "USER");
                response.put("userId", user.get().getUserId());
                return ResponseEntity.ok(response);
            }
        }

        // 2. Check Owner
        if (ownerService.isValidOwner(email, password)) {
            Optional<Owner> owner = ownerRepository.findByEmail(email);
            if (owner.isPresent()) {
                List<String> roles = List.of("ROLE_OWNER");
                String token = jwtUtil.generateToken(email, roles);

                response.put("message", "Login successful!");
                response.put("token", token);
                response.put("role", "OWNER");
                response.put("userId", owner.get().getOwnerId());
                return ResponseEntity.ok(response);
            }
        }

        // 3. Check Broker
        if (brokerService.isValidBroker(email, password)) {
            Optional<Broker> broker = brokerRepository.findByEmail(email);
            if (broker.isPresent()) {
                List<String> roles = List.of("ROLE_BROKER");
                String token = jwtUtil.generateToken(email, roles);

                response.put("message", "Login successful!");
                response.put("token", token);
                response.put("role", "BROKER");
                response.put("userId", broker.get().getBrokerId());
                return ResponseEntity.ok(response);
            }
        }

        // If none matched, return invalid credentials (This must be OUTSIDE all if blocks)
        response.put("message", "Invalid credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

}

