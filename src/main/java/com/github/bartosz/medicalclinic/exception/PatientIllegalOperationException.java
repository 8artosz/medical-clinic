package com.github.bartosz.medicalclinic.exception;

public class PatientIllegalOperationException extends PatientException {
    public PatientIllegalOperationException(){
        super("Patient illegal operation");
    }

}
