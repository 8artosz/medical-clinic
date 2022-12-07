package com.github.bartosz.medicalclinic.controller;

import com.github.bartosz.medicalclinic.exception.PatientNotFoundException;
import com.github.bartosz.medicalclinic.model.Patient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private final List<Patient> patients = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{email}")
    public Patient getPatientByEmail(@PathVariable("email") String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst()
                .orElseThrow(PatientNotFoundException::new);
    }

    @PostMapping
    public ResponseEntity<String> addPatient(@RequestBody Patient patient) {
        patients.add(patient);
        return ResponseEntity.ok("Added new patient");
    }

    @GetMapping("/id")
    public Patient getPatientById(@RequestParam long id) {
        return patients.stream()
                .filter(patient -> patient.getIdCardNo() == id)
                .findFirst()
                .orElseThrow(PatientNotFoundException::new);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deletePatientByEmail(@PathVariable("email") String email) {
        patients.removeIf(employee -> employee.getEmail().equals(email));

        return ResponseEntity.ok("Removed patient");
    }

    @PutMapping("/{email}")
    public ResponseEntity<String> editPatient(@PathVariable("email") String email, @RequestBody Patient newPatient) {
        var editedPatient = findPatientByEmail(email);

        editedPatient.setBirthday(newPatient.getBirthday());
        editedPatient.setEmail(newPatient.getEmail());
        editedPatient.setPhoneNumber(newPatient.getPhoneNumber());
        editedPatient.setFirstName(newPatient.getFirstName());
        editedPatient.setLastName(newPatient.getLastName());
        editedPatient.setPassword(newPatient.getPassword());

        return ResponseEntity.ok("Edited patient");
    }

    @PatchMapping("/{email}")
    public ResponseEntity<String> editPassword(@PathVariable("email") String email, @RequestBody String password) {
        var editedPatient = findPatientByEmail(email);
        editedPatient.setPassword(password);

        return ResponseEntity.ok("Changed password");
    }

    private Patient findPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst()
                .orElseThrow(PatientNotFoundException::new);
    }
}
