package com.github.bartosz.medicalclinic.service;

import com.github.bartosz.medicalclinic.exception.PasswordAlreadyExistsException;
import com.github.bartosz.medicalclinic.exception.PatientAlreadyExists;
import com.github.bartosz.medicalclinic.exception.PatientNotFoundException;
import com.github.bartosz.medicalclinic.model.Patient;
import com.github.bartosz.medicalclinic.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientByEmail(String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(PatientNotFoundException::new);
    }

    public void addPatient(Patient newPatient) {
        var patientWithGivenEmail = patientRepository.findByEmail(newPatient.getEmail());

        if (patientWithGivenEmail.isPresent()) {
            throw new PatientAlreadyExists();
        }

        patientRepository.save(newPatient);
    }

    public Patient getPatientById(long id) {
        return patientRepository.getPatientById(id)
                .orElseThrow(PatientNotFoundException::new);
    }

    public void deletePatientByEmail(String email) {
        patientRepository.deleteByEmail(email);
    }

    public void editPatient(String email, Patient newPatient) {

        if (!isPatientEditDataValid(newPatient, email)) {
            throw new PatientAlreadyExists();
        }

        patientRepository.update(email, newPatient);
    }

    public void editPassword(String email, String password) {
        var editedPatient = patientRepository.findByEmail(email)
                .orElseThrow(PatientNotFoundException::new);

        if (password.equals(editedPatient.getPassword())) {
            throw new PasswordAlreadyExistsException();
        }

        editedPatient.setPassword(password);

        patientRepository.update(email, editedPatient);
    }

    private boolean isPatientEditDataValid(Patient newPatient, String email) {
        if (email.equals(newPatient.getEmail())) {
            return true;
        }

        return patientRepository.findByEmail(newPatient.getEmail()).isEmpty();
    }
}
