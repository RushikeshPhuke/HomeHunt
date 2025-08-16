package com.rentalApp.services;

import com.rentalApp.entity.Broker;
import com.rentalApp.entity.Owner;
import com.rentalApp.entity.User;
import com.rentalApp.repository.BrokerRepository;
import com.rentalApp.repository.OwnerRepository;
import com.rentalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private BrokerRepository brokerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }

        Optional<Owner> ownerOpt = ownerRepository.findByEmail(email);
        if (ownerOpt.isPresent()) {
            Owner owner = ownerOpt.get();
            return new org.springframework.security.core.userdetails.User(
                    owner.getEmail(),
                    owner.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_OWNER"))
            );
        }

        Optional<Broker> brokerOpt = brokerRepository.findByEmail(email);
        if (brokerOpt.isPresent()) {
            Broker broker = brokerOpt.get();
            return new org.springframework.security.core.userdetails.User(
                    broker.getEmail(),
                    broker.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_BROKER"))
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
