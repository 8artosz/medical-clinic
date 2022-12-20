package com.github.bartosz.medicalclinic.handler;

import com.github.bartosz.medicalclinic.exception.PasswordAlreadyExistsException;
import com.github.bartosz.medicalclinic.exception.PatientAlreadyExists;
import com.github.bartosz.medicalclinic.exception.PatientNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PatientExceptionHandler {

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<String> patientNotFoundExceptionResponse(PatientNotFoundException patientNotFoundException) {
        return ResponseEntity.badRequest().body(patientNotFoundException.getMessage());
    }

    @ExceptionHandler(PatientAlreadyExists.class)
    public ResponseEntity<String> patientAlreadyExistsResponse(PatientAlreadyExists patientAlreadyExists) {
        return ResponseEntity.badRequest().body(patientAlreadyExists.getMessage());
    }

    @ExceptionHandler(PasswordAlreadyExistsException.class)
    public ResponseEntity<String> passwordAlreadyExistsException(PasswordAlreadyExistsException passwordAlreadyExistsException) {
        return ResponseEntity.badRequest().body(passwordAlreadyExistsException.getMessage());
    }
}
