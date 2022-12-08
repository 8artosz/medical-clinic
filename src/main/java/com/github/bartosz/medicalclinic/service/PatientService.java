package com.github.bartosz.medicalclinic.service;

import com.github.bartosz.medicalclinic.exception.PasswordAlreadyExistsException;
import com.github.bartosz.medicalclinic.exception.PatientAlreadyExists;
import com.github.bartosz.medicalclinic.exception.PatientNotFoundException;
import com.github.bartosz.medicalclinic.model.Patient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final List<Patient> patients = new ArrayList<>();

    public List<Patient> getAllPatients() {
        return patients;
    }

    public Patient getPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst()
                .orElseThrow(PatientNotFoundException::new);
    }

    public void addPatient(Patient newPatient) {
        var patientWithGivenEmail = patients.stream()
                .filter(patient -> patient.getEmail().equals(newPatient.getEmail()))
                .findFirst();

        if (patientWithGivenEmail.isPresent()) {
            throw new PatientAlreadyExists();
        }

        patients.add(newPatient);
    }

    public Patient getPatientById(long id) {
        return patients.stream()
                .filter(patient -> patient.getIdCardNo() == id)
                .findFirst()
                .orElseThrow(PatientNotFoundException::new);
    }

    public void deletePatientByEmail(String email) {
        patients.removeIf(employee -> employee.getEmail().equals(email));
    }

    public void editPatient(String email, Patient newPatient) {

        if (!isPatientEditDataValid(newPatient, email)) {
            throw new PatientAlreadyExists();
        }

        var editedPatient = findPatientByEmail(email)
                .orElseThrow(PatientNotFoundException::new);

        editedPatient.setBirthday(newPatient.getBirthday());
        editedPatient.setEmail(newPatient.getEmail());
        editedPatient.setPhoneNumber(newPatient.getPhoneNumber());
        editedPatient.setFirstName(newPatient.getFirstName());
        editedPatient.setLastName(newPatient.getLastName());
        editedPatient.setPassword(newPatient.getPassword());
    }

    public void editPassword(String email, String password) {
        var editedPatient = findPatientByEmail(email)
                .orElseThrow(PatientNotFoundException::new);

        if (password.equals(editedPatient.getPassword())) {
            throw new PasswordAlreadyExistsException();
        }

        editedPatient.setPassword(password);
    }

    private Optional<Patient> findPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }

    private boolean isPatientEditDataValid(Patient newPatient, String email) {
        if (email.equals(newPatient.getEmail())) {
            return true;
        }

        var newPatientWithGivenEmail = patients.stream()
                .filter(patient -> patient.getEmail().equals(newPatient.getEmail()))
                .findFirst();

        return newPatientWithGivenEmail.isEmpty();
    }
}
