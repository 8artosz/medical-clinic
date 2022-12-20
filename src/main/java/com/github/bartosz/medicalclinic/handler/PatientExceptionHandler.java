package com.github.bartosz.medicalclinic.handler;

import com.github.bartosz.medicalclinic.exception.PatientException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PatientExceptionHandler {

    @ExceptionHandler(PatientException.class)
    public ResponseEntity<String> patient(PatientException patientException) {
        return ResponseEntity.badRequest().body(patientException.getMessage());
    }
}
