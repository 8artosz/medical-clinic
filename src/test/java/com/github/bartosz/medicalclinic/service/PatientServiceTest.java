package com.github.bartosz.medicalclinic.service;

import com.github.bartosz.medicalclinic.dto.PatientDto;
import com.github.bartosz.medicalclinic.exception.PasswordAlreadyExistsException;
import com.github.bartosz.medicalclinic.exception.PatientAlreadyExists;
import com.github.bartosz.medicalclinic.exception.PatientIllegalOperationException;
import com.github.bartosz.medicalclinic.exception.PatientNotFoundException;
import com.github.bartosz.medicalclinic.model.Patient;
import com.github.bartosz.medicalclinic.repository.PatientRepository;
import com.github.bartosz.medicalclinic.util.PatientUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PatientServiceTest {

    private PatientService patientService;
    private ModelMapper modelMapper;
    @MockBean
    private PatientRepository patientRepository;

    @BeforeEach
    void setup() {
        modelMapper = new ModelMapper();
        patientService = new PatientService(patientRepository, modelMapper);
    }

    @Test
    void getAllPatients_CorrectData_ReturnData() {
        var patients = List.of(PatientUtils.buildPatient(PatientUtils.buildPatientDto()));
        when(patientRepository.findAll()).thenReturn(patients);

        var result = patientService.getAllPatients();
        var expectedResult = patients.stream()
                .map(patient -> modelMapper.map(patient, PatientDto.class))
                .collect(Collectors.toList());

        assertNotNull(result);
        assertEquals(1, result.size());
        checkPatientsDto(expectedResult, result);
    }

    @Test
    void getPatientByEmail_CorrectData_ReturnData() {
        var patient = PatientUtils.buildPatient(PatientUtils.buildPatientDto());
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(patient));

        var result = patientService.getPatientByEmail("test@gmail.com");

        assertNotNull(result);
        checkPatientDto(modelMapper.map(patient, PatientDto.class), result);
    }

    @Test
    void getPatientByEmail_PatientDoesNotExist_ThrowException() {
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class, () -> patientService.getPatientByEmail("test@gmail.com"));

        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    void getPatientById_CorrectData_ReturnData() {
        var patient = PatientUtils.buildPatient(PatientUtils.buildPatientDto());
        when(patientRepository.getPatientById(anyLong())).thenReturn(Optional.of(patient));

        var result = patientService.getPatientById(1);

        assertNotNull(result);
        checkPatientDto(modelMapper.map(patient, PatientDto.class), result);
    }

    @Test
    void getPatientById_PatientDoesNotExist_ThrowException() {
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class, () -> patientService.getPatientById(1));

        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    void addPatient_CorrectData_SaveInvoked() {
        var patient = PatientUtils.buildPatientDto();
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        doNothing().when(patientRepository).save(any());

        patientService.addPatient(patient);

        verify(patientRepository).save(modelMapper.map(patient, Patient.class));
    }

    @Test
    void addPatient_PatientAlreadyExist_ThrowException() {
        var patient = PatientUtils.buildPatientDto();
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(PatientUtils.buildPatient(patient)));

        PatientAlreadyExists exception = assertThrows(PatientAlreadyExists.class, () -> patientService.addPatient(patient));

        assertEquals("Patient with given email already exists", exception.getMessage());
    }

    @Test
    void editPatient_CorrectData_UpdateInvoked() {
        var patient = PatientUtils.buildPatientDto();
        var email = patient.getEmail();
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(PatientUtils.buildPatient(patient)));
        doNothing().when(patientRepository).update(anyString(), any(Patient.class));

        patientService.editPatient(email, patient);

        verify(patientRepository).update(email, modelMapper.map(patient, Patient.class));
    }

    @Test
    void editPatient_PatientWithGivenEmailExists_ThrowException() {
        var patient = PatientUtils.buildPatientDto();
        var email = "test1@gmail.com";
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(PatientUtils.buildPatient(patient)));

        PatientAlreadyExists exception = assertThrows(PatientAlreadyExists.class, () -> patientService.editPatient(email, patient));

        assertEquals("Patient with given email already exists", exception.getMessage());
    }

    @Test
    void editPatient_PatientIdCardNoEdited_ThrowException() {
        var patient = PatientUtils.buildPatientDto();
        var patientWithEditedIdCardNo = PatientUtils.buildPatientDto();
        patientWithEditedIdCardNo.setIdCardNo(2L);
        var email = "test@gmail.com";
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(PatientUtils.buildPatient(patient)));

        PatientIllegalOperationException exception = assertThrows(PatientIllegalOperationException.class, () -> patientService.editPatient(email, patientWithEditedIdCardNo));

        assertEquals("Patient illegal operation", exception.getMessage());
    }

    @Test
    void deletePatientByEmail_CorrectData_DeleteByEmailInvoked() {
        var email = "test@gmail.com";
        doNothing().when(patientRepository).deleteByEmail(anyString());

        patientService.deletePatientByEmail(email);

        verify(patientRepository).deleteByEmail(email);
    }

    @Test
    void editPassword_CorrectData_UpdateInvoked() {
        var patient = PatientUtils.buildPatientDto();
        var email = "test@gmail.com";
        var password = "test1";
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(PatientUtils.buildPatient(patient)));
        doNothing().when(patientRepository).update(anyString(), any(Patient.class));

        patientService.editPassword(email, password);

        patient.setPassword(password);
        verify(patientRepository).update(email, modelMapper.map(patient, Patient.class));
    }

    @Test
    void editPassword_PatientDoesNotExist_ThrowException() {
        var email = "test@gmail.com";
        var password = "test";
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class, () -> patientService.editPassword(email, password));

        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    void editPassword_PasswordAlreadyExist_ThrowException() {
        var patient = PatientUtils.buildPatientDto();
        var email = "test@gmail.com";
        var password = "test";
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(PatientUtils.buildPatient(patient)));

        PasswordAlreadyExistsException exception = assertThrows(PasswordAlreadyExistsException.class, () -> patientService.editPassword(email, password));

        assertEquals("Password already exists", exception.getMessage());
    }

    private void checkPatientsDto(List<PatientDto> expectedPatients, List<PatientDto> actualPatients) {
        int i = 0;
        for (var expectedPatient : expectedPatients) {
            checkPatientDto(expectedPatient, actualPatients.get(i));
            i++;
        }
    }

    private void checkPatientDto(PatientDto expectedPatient, PatientDto actualPatient) {

        assertEquals(expectedPatient.getEmail(), actualPatient.getEmail());
        assertEquals(expectedPatient.getPassword(), actualPatient.getPassword());
        assertEquals(expectedPatient.getFirstName(), actualPatient.getFirstName());
        assertEquals(expectedPatient.getLastName(), actualPatient.getLastName());
        assertEquals(expectedPatient.getPhoneNumber(), actualPatient.getPhoneNumber());
        assertEquals(expectedPatient.getIdCardNo(), actualPatient.getIdCardNo());
        assertEquals(expectedPatient.getBirthday(), actualPatient.getBirthday());
    }
}
