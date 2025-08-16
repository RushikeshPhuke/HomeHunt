package com.rentalApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages="com.rentalApp,com.rentalApp.controller,com.rentalApp.services")
@EntityScan(basePackages="com.rentalApp.entity")
@EnableJpaRepositories(basePackages="com.rentalApp.repository")
public class PropertyRentalSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(PropertyRentalSystemApplication.class, args);
	}

}

