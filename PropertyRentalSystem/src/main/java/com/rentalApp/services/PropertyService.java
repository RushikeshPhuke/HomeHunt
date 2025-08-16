package com.rentalApp.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rentalApp.entity.Property;
import com.rentalApp.repository.PropertyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepo;

    public Property addProperty(Property property) {
        return propertyRepo.save(property);
    }

    public Optional<Property> getPropertyById(int id) {
        return propertyRepo.findById(id);
    }

    public List<Property> searchProperties(String city, String type, float maxPrice) {
        return propertyRepo.findByCityContainingIgnoreCaseAndPropertyTypeContainingIgnoreCaseAndPriceLessThanEqual(
                city, type, maxPrice);
    }

    public List<Property> getAllProperties() {
        return propertyRepo.findAll();
    }
}

