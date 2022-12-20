package com.github.bartosz.medicalclinic.service;

import com.github.bartosz.medicalclinic.exception.PasswordAlreadyExistsException;
import com.github.bartosz.medicalclinic.exception.PatientAlreadyExists;
import com.github.bartosz.medicalclinic.exception.PatientNotFoundException;
import com.github.bartosz.medicalclinic.model.Patient;
import com.github.bartosz.medicalclinic.repository.PatientRepository;
import com.github.bartosz.medicalclinic.util.PatientUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PatientServiceTest {

    private PatientService patientService;
    @MockBean
    private PatientRepository patientRepository;

    @BeforeEach
    void setup() {
        patientService = new PatientService(patientRepository);
    }

    @Test
    void getAllPatients_CorrectData_ReturnData() {
        var patients = List.of(PatientUtils.buildPatient());
        when(patientRepository.findAll()).thenReturn(patients);

        var result = patientService.getAllPatients();

        assertNotNull(result);
        assertEquals(1, result.size());
        checkPatients(patients, result);
    }

    @Test
    void getPatientByEmail_CorrectData_ReturnData() {
        var patient = PatientUtils.buildPatient();
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(patient));

        var result = patientService.getPatientByEmail("test@gmail.com");

        assertNotNull(result);
        checkPatient(patient, result);
    }

    @Test
    void getPatientByEmail_PatientDoesNotExist_ThrowException() {
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.empty());

       PatientNotFoundException exception =  assertThrows(PatientNotFoundException.class,() -> patientService.getPatientByEmail("test@gmail.com"));

       assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    void getPatientById_CorrectData_ReturnData() {
        var patient = PatientUtils.buildPatient();
        when(patientRepository.getPatientById(anyLong())).thenReturn(Optional.of(patient));

        var result = patientService.getPatientById(1);

        assertNotNull(result);
        checkPatient(patient, result);
    }

    @Test
    void getPatientById_PatientDoesNotExist_ThrowException() {
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        PatientNotFoundException exception =  assertThrows(PatientNotFoundException.class,() -> patientService.getPatientById(1));

        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    void addPatient_CorrectData_SaveInvoked() {
        var patient = PatientUtils.buildPatient();
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        doNothing().when(patientRepository).save(any());

        patientService.addPatient(patient);

        verify(patientRepository).save(patient);
    }

    @Test
    void addPatient_PatientAlreadyExist_ThrowException() {
        var patient = PatientUtils.buildPatient();
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(patient));

        PatientAlreadyExists exception = assertThrows(PatientAlreadyExists.class, () -> patientService.addPatient(patient));

        assertEquals("Patient with given email already exists", exception.getMessage());
    }

    @Test
    void editPatient_CorrectData_UpdateInvoked() {
        var patient = PatientUtils.buildPatient();
        var email = patient.getEmail();
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(patient));
        doNothing().when(patientRepository).update(anyString(), any(Patient.class));

        patientService.editPatient(email, patient);

        verify(patientRepository).update(email, patient);
    }

    @Test
    void editPatient_PatientWithGivenEmailExists_ThrowException() {
        var patient = PatientUtils.buildPatient();
        var email = "test1@gmail.com";
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(patient));

        PatientAlreadyExists exception = assertThrows(PatientAlreadyExists.class, () -> patientService.editPatient(email,patient));

        assertEquals("Patient with given email already exists", exception.getMessage());
    }

    @Test
    void editPatient_PatientDoesNotExist_ThrowException() {
        var patient = PatientUtils.buildPatient();
        var email = patient.getEmail();
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class, () -> patientService.editPatient(email,patient));

        assertEquals("Patient not found", exception.getMessage());
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
        var patient = PatientUtils.buildPatient();
        var email = "test@gmail.com";
        var password = "test1";
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(patient));
        doNothing().when(patientRepository).update(anyString(), any(Patient.class));

        patientService.editPassword(email, password);

        verify(patientRepository).update(email, patient);
    }

    @Test
    void editPassword_PatientDoesNotExist_ThrowException() {
        var email = "test@gmail.com";
        var password = "test";
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class, () -> patientService.editPassword(email,password));

        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    void editPassword_PasswordAlreadyExist_ThrowException() {
        var patient = PatientUtils.buildPatient();
        var email = "test@gmail.com";
        var password = "test";
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(patient));

        PasswordAlreadyExistsException exception = assertThrows(PasswordAlreadyExistsException.class, () -> patientService.editPassword(email,password));

        assertEquals("Password already exists", exception.getMessage());
    }

    private void checkPatients(List<Patient> expectedPatients, List<Patient> actualPatients) {
        int i = 0;
        for (var expectedPatient : expectedPatients) {
            checkPatient(expectedPatient, actualPatients.get(i));
            i++;
        }
    }

    private void checkPatient(Patient expectedPatient, Patient actualPatient) {

        assertEquals(expectedPatient.getEmail(), actualPatient.getEmail());
        assertEquals(expectedPatient.getPassword(), actualPatient.getPassword());
        assertEquals(expectedPatient.getFirstName(), actualPatient.getFirstName());
        assertEquals(expectedPatient.getLastName(), actualPatient.getLastName());
        assertEquals(expectedPatient.getPhoneNumber(), actualPatient.getPhoneNumber());
        assertEquals(expectedPatient.getIdCardNo(), actualPatient.getIdCardNo());
        assertEquals(expectedPatient.getBirthday(), actualPatient.getBirthday());
    }
}
