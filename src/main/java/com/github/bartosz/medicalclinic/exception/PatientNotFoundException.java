package com.github.bartosz.medicalclinic.exception;

public class PatientNotFoundException extends PatientException {
    public PatientNotFoundException() {
        super("Patient not found");
    }
}
