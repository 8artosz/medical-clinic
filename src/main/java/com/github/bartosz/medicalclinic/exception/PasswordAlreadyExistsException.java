package com.github.bartosz.medicalclinic.exception;

public class PasswordAlreadyExistsException extends PatientException {

    public PasswordAlreadyExistsException(){
        super("Password already exists");
    }
}
