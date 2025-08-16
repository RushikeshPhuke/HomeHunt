package com.rentalApp.dto;



import java.time.LocalDate;


public class BrokerDTO {
    private Integer brokerId;
    private String name;
    private String email;
    private String phone;
    private int experienceYears;
    private LocalDate registeredDate;

    
    
    
    public BrokerDTO(Integer brokerId, String name, String email, String phone, int experienceYears,
			LocalDate registeredDate) {
		super();
		this.brokerId = brokerId;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.experienceYears = experienceYears;
		this.registeredDate = registeredDate;
	}

	public Integer getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(Integer brokerId) {
        this.brokerId = brokerId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public LocalDate getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDate registeredDate) {
        this.registeredDate = registeredDate;
    }
}

