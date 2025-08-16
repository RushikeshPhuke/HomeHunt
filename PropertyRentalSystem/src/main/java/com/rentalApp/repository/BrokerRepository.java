package com.rentalApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentalApp.entity.Broker;
import com.rentalApp.entity.User;

@Repository
public interface BrokerRepository extends JpaRepository<Broker, Integer>{
	Optional<Broker> findByEmailAndPassword(String email, String password);
	Optional<Broker> findByEmail(String email);

}
