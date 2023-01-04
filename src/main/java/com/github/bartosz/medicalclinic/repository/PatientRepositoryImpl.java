package com.github.bartosz.medicalclinic.repository;

import com.github.bartosz.medicalclinic.exception.PatientIllegalOperationException;
import com.github.bartosz.medicalclinic.exception.PatientNotFoundException;
import com.github.bartosz.medicalclinic.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepositoryImpl implements PatientRepository {

    private final List<Patient> patients = new ArrayList<>();

    @Override
    public List<Patient> findAll() {
        return patients;
    }

    @Override
    public void save(Patient patient) {
        if (!patient.isPatientValid()) {
            throw new PatientIllegalOperationException();
        }
        patients.add(patient);
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<Patient> getPatientById(long id) {
        return patients.stream()
                .filter(patient -> patient.getIdCardNo() == id)
                .findFirst();
    }

    @Override
    public void deleteByEmail(String email) {
        findByEmail(email).ifPresent(patients::remove);
    }

    @Override
    public boolean existsByEmail(String email) {
        return patients.stream()
                .map(Patient::getEmail)
                .anyMatch(patientEmail -> patientEmail.equals(email));
    }

    @Override
    public void update(String email, Patient patient) {
        if (!patient.isPatientValid()) {
            throw new PatientIllegalOperationException();
        }

        var entity = findByEmail(email)
                .orElseThrow(PatientNotFoundException::new);

        entity.setPassword(patient.getPassword());
        entity.setBirthday(patient.getBirthday());
        entity.setFirstName(patient.getFirstName());
        entity.setLastName(patient.getLastName());
        entity.setPhoneNumber(patient.getPhoneNumber());
        entity.setEmail(patient.getEmail());
        entity.setIdCardNo(patient.getIdCardNo());
    }
}
