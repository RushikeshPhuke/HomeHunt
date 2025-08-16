package com.rentalApp.dto;

import java.time.LocalDate;
import java.util.List;

import com.rentalApp.entity.Property;


public class OwnerDto {
	
	
	private Integer ownerId;

	
    private String name;
	
    private String email;
	
    private String password;
	
    private String phone;
	
    private LocalDate registrationDate;

	
    private Integer brokerId;
	
	
    private List<Property> properties;

	public OwnerDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OwnerDto(Integer ownerId, String name, String email, String password, String phone, LocalDate registrationDate,
			Integer brokerId, List<Property> properties) {
		super();
		this.ownerId = ownerId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.registrationDate = registrationDate;
		this.brokerId = brokerId;
		this.properties = properties;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
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

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Integer getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(int brokerId) {
		this.brokerId = brokerId;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	
	

}
