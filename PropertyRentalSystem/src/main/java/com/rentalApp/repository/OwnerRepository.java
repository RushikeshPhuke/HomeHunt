package com.rentalApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentalApp.entity.Broker;
import com.rentalApp.entity.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner,Integer>{

	//List<Owner> findByBroker_BrokerId(Integer brokerId);
	//List<Owner> findByBroker_BrokerId(Integer brokerId);
	List<Owner> findByBrokerId(Integer brokerId);
	Owner findByEmailAndPassword(String email, String password);
	//changed
	//List<Owner> findByBrokerBrokerId(Integer brokerId);
	Optional<Owner> findByEmail(String email);

	
	



}
