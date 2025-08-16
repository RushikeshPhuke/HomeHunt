package com.rentalApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rentalApp.entity.Owner;
import com.rentalApp.entity.User;
import com.rentalApp.security.JwtUtil;
import com.rentalApp.services.BrokerService;
import com.rentalApp.services.OwnerService;
import com.rentalApp.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	OwnerService ownerService;
	
	@Autowired
	BrokerService brokerService;


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping("/addUser")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

   

    @GetMapping("/broker/{brokerId}")
    public List<User> getUsersByBroker(@PathVariable Integer brokerId) {
        return userService.getUsersByBroker(brokerId);
    }
    
    @PostMapping("/user/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Map<String, Object> response = new HashMap<>();

        // 1. Check User
        if (userService.isValidUser(email, password)) {
            List<String> roles = List.of("ROLE_USER");
            String token = jwtUtil.generateToken(email, roles);

            response.put("message", "Login successful!");
            response.put("token", token);
            response.put("role", "USER");
            return ResponseEntity.ok(response);
        }

        // 2. Check Owner
        Owner owner = ownerService.getOwnerByEmailAndPassword(email, password);
        if (owner != null) {
            List<String> roles = List.of("ROLE_OWNER");
            String token = jwtUtil.generateToken(email, roles);

            response.put("message", "Login successful!");
            response.put("token", token);
            response.put("role", "OWNER");
            return ResponseEntity.ok(response);
        }

        // 3. Check Broker
        if (brokerService.isValidBroker(email, password)) {
            List<String> roles = List.of("ROLE_BROKER");
            String token = jwtUtil.generateToken(email, roles);

            response.put("message", "Login successful!");
            response.put("token", token);
            response.put("role", "BROKER");
            return ResponseEntity.ok(response);
        }

        // If none matched
        response.put("message", "Invalid credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }


    }



