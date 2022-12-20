package com.github.bartosz.medicalclinic.exception;

public class PatientAlreadyExists extends PatientException {

    public PatientAlreadyExists(){
        super("Patient with given email already exists");
    }
}
