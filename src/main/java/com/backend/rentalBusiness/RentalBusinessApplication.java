package com.backend.rentalBusiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RentalBusinessApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentalBusinessApplication.class, args);
		System.out.println("Rental Business Application started");
		System.out.println("http://localhost:5000");
	}

}
