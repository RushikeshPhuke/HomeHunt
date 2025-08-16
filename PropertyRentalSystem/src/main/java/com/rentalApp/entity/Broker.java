package com.rentalApp.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "broker")
public class Broker {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer brokerId;

    private String name;
    private String email;
    private String password;
    private String phone;
    private int experienceYears;
    private LocalDate registeredDate;
    
    // changed
    @OneToMany(mappedBy = "broker", cascade = CascadeType.ALL)
    @JsonBackReference("broker-properties")
    private List<Property> properties;
    
    public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	

	

    
    public Broker() {
		// TODO Auto-generated constructor stub
	}
    
    public Broker(Integer brokerId,String name, String email, String password,String phone, int experienceYears,
			LocalDate registeredDate) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone=phone;
		this.experienceYears = experienceYears;
		this.registeredDate = registeredDate;
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
    
    public Integer getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(Integer brokerId) {
        this.brokerId = brokerId;
    }

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
    
    


	
}

