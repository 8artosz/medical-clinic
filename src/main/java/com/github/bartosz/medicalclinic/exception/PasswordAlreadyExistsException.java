package com.github.bartosz.medicalclinic.exception;

public class PasswordAlreadyExistsException extends RuntimeException {

    public PasswordAlreadyExistsException(){
        super("Password already exists");
    }
}
