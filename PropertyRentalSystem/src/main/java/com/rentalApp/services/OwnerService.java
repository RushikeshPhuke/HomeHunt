package com.rentalApp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rentalApp.dto.OwnerDto;
import com.rentalApp.entity.Broker;
import com.rentalApp.entity.Owner;

@Service
public interface OwnerService {
	
	public boolean addOwner(Owner owner);
	public boolean isValidOwner(String email, String rawPassword);
	public Owner getOwnerByEmailAndPassword(String email, String password) ;
	

}
