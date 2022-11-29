package com.github.bartosz.medicalclinic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MedicalClinicApplication {

	@Autowired
	public static void main(String[] args) {
		SpringApplication.run(MedicalClinicApplication.class, args);
	}

}
