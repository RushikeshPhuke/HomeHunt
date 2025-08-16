package com.rentalApp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rentalApp.dto.BrokerDTO;
import com.rentalApp.entity.Broker;
import com.rentalApp.entity.Owner;

@Service
public interface BrokerService {
	
	public boolean addBroker(Broker broker);
    public List<Broker> getAllBrokers();
    public List<Owner> getOwnersByBrokerId(Integer brokerId);
    public boolean isValidBroker(String email, String rawPassword);
    public Broker getBrokerById(Integer brokerId);


}
