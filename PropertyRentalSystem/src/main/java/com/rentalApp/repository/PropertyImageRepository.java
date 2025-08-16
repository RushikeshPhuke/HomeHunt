package com.rentalApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentalApp.entity.PropertyImage;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Integer>{
	
	List<PropertyImage> findByProperty_PropertyId(int propertyId);

}
