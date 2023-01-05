package com.github.bartosz.medicalclinic.repository;

import com.github.bartosz.medicalclinic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByEmail(String email);

    void deleteByEmail(String email);
}