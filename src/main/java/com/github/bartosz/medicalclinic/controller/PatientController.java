package com.github.bartosz.medicalclinic.controller;

import com.github.bartosz.medicalclinic.model.Patient;
import com.github.bartosz.medicalclinic.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{email}")
    public ResponseEntity<Patient> getPatientByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(patientService.getPatientByEmail(email));
    }

    @PostMapping
    public ResponseEntity<String> addPatient(@RequestBody Patient patient) {
        patientService.addPatient(patient);
        return ResponseEntity.ok("Added new patient");
    }

    @GetMapping("/id")
    public ResponseEntity<Patient> getPatientById(@RequestParam long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deletePatientByEmail(@PathVariable("email") String email) {
        patientService.deletePatientByEmail(email);

        return ResponseEntity.ok("Removed patient");
    }

    @PutMapping("/{email}")
    public ResponseEntity<String> editPatient(@PathVariable("email") String email, @RequestBody Patient newPatient) {
        patientService.editPatient(email, newPatient);

        return ResponseEntity.ok("Edited patient");
    }

    @PatchMapping("/{email}")
    public ResponseEntity<String> editPassword(@PathVariable("email") String email, @RequestBody String password) {
        patientService.editPassword(email, password);

        return ResponseEntity.ok("Changed password");
    }
}
