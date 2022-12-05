package com.github.bartosz.medicalclinic.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException() {
        super("Patient not found");
    }
}
