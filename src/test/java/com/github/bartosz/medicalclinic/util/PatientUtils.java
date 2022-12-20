package com.github.bartosz.medicalclinic.util;

import com.github.bartosz.medicalclinic.model.Patient;

import java.time.LocalDate;

public final class PatientUtils {

    private PatientUtils() {}

    public static Patient buildPatient() {
        return Patient.builder()
                .email("test@gmail.com")
                .password("test")
                .birthday(LocalDate.now())
                .firstName("Test1")
                .lastName("Test2")
                .phoneNumber("821222838")
                .idCardNo(1L)
                .build();
    }
}
