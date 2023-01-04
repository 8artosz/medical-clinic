package com.github.bartosz.medicalclinic.util;

import com.github.bartosz.medicalclinic.dto.PatientDto;
import com.github.bartosz.medicalclinic.model.Patient;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

public final class PatientUtils {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();
    private PatientUtils() {}

    public static PatientDto buildPatientDto() {
        return PatientDto.builder()
                .email("test@gmail.com")
                .password("test")
                .birthday(LocalDate.now())
                .firstName("Test1")
                .lastName("Test2")
                .phoneNumber("821222838")
                .idCardNo(1L)
                .build();
    }

    public static Patient buildPatient(PatientDto patientDto) {
        return MODEL_MAPPER.map(patientDto, Patient.class);
    }
}
