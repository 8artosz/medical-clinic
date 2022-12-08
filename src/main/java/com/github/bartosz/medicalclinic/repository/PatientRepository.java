package com.github.bartosz.medicalclinic.repository;

import com.github.bartosz.medicalclinic.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientRepository {

    List<Patient> findAll();

    void save(Patient patient);

    Optional<Patient> findByEmail(String email);

    Optional<Patient> getPatientById(long id);

    void deleteByEmail(String email);

    boolean existsByEmail(String email);

    void update(String email, Patient patient);
}