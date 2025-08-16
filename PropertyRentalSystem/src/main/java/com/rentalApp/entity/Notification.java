package com.rentalApp.entity;

import jakarta.persistence.*;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer brokerId;
    private String message;
    private boolean readStatus = false;

    // New fields
    private String userName;
    private String userPhone;

    public Notification() {}

    public Notification(Integer brokerId, String message, String userName, String userPhone) {
        this.brokerId = brokerId;
        this.message = message;
        this.userName = userName;
        this.userPhone = userPhone;
    }

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getBrokerId() { return brokerId; }
    public void setBrokerId(Integer brokerId) { this.brokerId = brokerId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isReadStatus() { return readStatus; }
    public void setReadStatus(boolean readStatus) { this.readStatus = readStatus; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }
}
