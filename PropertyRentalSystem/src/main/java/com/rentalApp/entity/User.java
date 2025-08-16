package com.rentalApp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer userId;

	    private String name;
	    private String email;
	    private String password;
	    private String phone;

	    private LocalDateTime registrationDate;
	    
	    @ManyToOne
	    @JoinColumn(name = "broker_id")  // foreign key column
	    private Broker broker;

		public User(Integer userId, String name, String email, String password, String phone,
				LocalDateTime registrationDate, Broker broker) {
			super();
			this.userId = userId;
			this.name = name;
			this.email = email;
			this.password = password;
			this.phone = phone;
			this.registrationDate = registrationDate;
			this.broker = broker;
		}

		public User() {
			super();
		}

		public Integer getUserId() {
			return userId;
		}

		public void setUserId(Integer userId) {
			this.userId = userId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public LocalDateTime getRegistrationDate() {
			return registrationDate;
		}

		public void setRegistrationDate(LocalDateTime registrationDate) {
			this.registrationDate = registrationDate;
		}

		public Broker getBroker() {
			return broker;
		}

		public void setBroker(Broker broker) {
			this.broker = broker;
		}

		
	    
	    
	}



