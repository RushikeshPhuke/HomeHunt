package com.rentalApp.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rentalApp.dto.BrokerDTO;
import com.rentalApp.entity.Broker;
import com.rentalApp.entity.Owner;
import com.rentalApp.entity.User;
import com.rentalApp.repository.BrokerRepository;
import com.rentalApp.repository.OwnerRepository;

@Service
public class BrokerServiceImplementation implements BrokerService{
	
	@Autowired
    private BrokerRepository brokerRepository;
	
	@Autowired
    private OwnerRepository ownerRepo;

	@Autowired
    private BCryptPasswordEncoder passwordEncoder;  // inject password encoder

    @Override
    public boolean addBroker(Broker broker) {
        try {
            // Encode password before saving
            if (broker.getPassword() != null && !broker.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(broker.getPassword());
                broker.setPassword(encodedPassword);
            }

            // Set registration date
            broker.setRegisteredDate(LocalDate.now());

            // Save broker to DB
            brokerRepository.save(broker);
            System.out.println("Broker added: " + broker);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public List<Broker> getAllBrokers() {
        return brokerRepository.findAll();
    }

    @Override
    public List<Owner> getOwnersByBrokerId(Integer brokerId) {
        if (!brokerRepository.existsById(brokerId)) {
            throw new RuntimeException("Broker with ID " + brokerId + " not found.");
        }
        return ownerRepo.findByBrokerId(brokerId);
    }
    
    public boolean isValidBroker(String email, String rawPassword) {
      Optional<Broker> brokerOpt = brokerRepository.findByEmail(email);
        if (brokerOpt.isPresent()) {
            Broker broker = brokerOpt.get();
            // Compare raw password with encoded stored password
            return passwordEncoder.matches(rawPassword, broker.getPassword());
        }
        return false;
    }
    
    public Broker getBrokerById(Integer brokerId) {
        return brokerRepository.findById(brokerId).orElse(null);
    }


}
