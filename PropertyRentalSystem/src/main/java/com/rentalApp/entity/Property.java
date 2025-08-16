package com.rentalApp.entity;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="property")
public class Property {
	
	public List<PropertyImage> getImages() {
		return images;
	}

	public void setImages(List<PropertyImage> images) {
		this.images = images;
	}
	
	//broker button 
		@ManyToOne
		@JoinColumn(name = "broker_id")
		@JsonBackReference(value = "broker-properties")
		private Broker broker;
		
		
		//getter setter for broker
		public Broker getBroker() {
			return broker;
		}

		public void setBroker(Broker broker) {
			this.broker = broker;
		}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int propertyId;

	@Column
    private String title;
	@Column
    private String description;
	@Column
    private float price;
	@Column
    private String propertyType;
	@Column
    private String status; // e.g., Available, Sold, Rented
	@Column
    private String address;
	@Column
    private String city;
	@Column
    private String location;
	@Column
    private int bedrooms;
	@Column
    private int bathrooms;
	@Column
    private float area;
	@Column
    private boolean furnished;
	@Column
    private LocalDateTime postedDate;
	
    @Column(name = "sale_or_rent")
    private String saleOrRent; // "Sale" or "Rent"

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonBackReference(value = "property-owner") // changed
    private Owner owner;


    
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "property-images")
    private List<PropertyImage> images;



	public Property() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Property(int propertyId, String title, String description, float price, String propertyType, String status,
			String address, String city, String location, int bedrooms, int bathrooms, float area, boolean furnished,
			LocalDateTime postedDate, String saleOrRent, Owner owner) {
		super();
		this.propertyId = propertyId;
		this.title = title;
		this.description = description;
		this.price = price;
		this.propertyType = propertyType;
		this.status = status;
		this.address = address;
		this.city = city;
		this.location = location;
		this.bedrooms = bedrooms;
		this.bathrooms = bathrooms;
		this.area = area;
		this.furnished = furnished;
		this.postedDate = postedDate;
		this.saleOrRent = saleOrRent;
		this.owner = owner;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getBedrooms() {
		return bedrooms;
	}

	public void setBedrooms(int bedrooms) {
		this.bedrooms = bedrooms;
	}

	public int getBathrooms() {
		return bathrooms;
	}

	public void setBathrooms(int bathrooms) {
		this.bathrooms = bathrooms;
	}

	public float getArea() {
		return area;
	}

	public void setArea(float area) {
		this.area = area;
	}

	public boolean isFurnished() {
		return furnished;
	}

	public void setFurnished(boolean furnished) {
		this.furnished = furnished;
	}

	public LocalDateTime getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(LocalDateTime localDateTime) {
		this.postedDate = localDateTime;
	}

	public String getSaleOrRent() {
		return saleOrRent;
	}

	public void setSaleOrRent(String saleOrRent) {
		this.saleOrRent = saleOrRent;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	
    
    

}
