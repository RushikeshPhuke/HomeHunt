package com.rentalApp.entity;


import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="owner")
public class Owner {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer ownerId;

	@Column
    private String name;
	@Column
    private String email;
	@Column
    private String password;
	@Column
    private String phone;
	@Column
    private LocalDate registrationDate;

	
	@Column
	
    private Integer brokerId;
	
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	@JsonManagedReference(value = "property-owner") // changed
	private List<Property> properties;
	

	public Owner() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Owner(Integer ownerId, String name, String email, String password, String phone, LocalDate registrationDate,
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

	public void setBrokerId(Integer brokerId) {
		this.brokerId = brokerId;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	
	

}
