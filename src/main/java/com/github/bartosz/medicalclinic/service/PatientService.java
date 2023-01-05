package com.github.bartosz.medicalclinic.service;

import com.github.bartosz.medicalclinic.dto.PatientDto;
import com.github.bartosz.medicalclinic.entity.Patient;
import com.github.bartosz.medicalclinic.exception.PasswordAlreadyExistsException;
import com.github.bartosz.medicalclinic.exception.PatientAlreadyExists;
import com.github.bartosz.medicalclinic.exception.PatientIllegalOperationException;
import com.github.bartosz.medicalclinic.exception.PatientNotFoundException;
import com.github.bartosz.medicalclinic.mappers.PatientMapper;
import com.github.bartosz.medicalclinic.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patient -> patientMapper.patientToPatientDto(patient))
                .collect(Collectors.toList());
    }

    public PatientDto getPatientByEmail(String email) {
        var patient = patientRepository.findByEmail(email)
                .orElseThrow(PatientNotFoundException::new);
        return patientMapper.patientToPatientDto(patient);
    }

    @Transactional
    public void addPatient(PatientDto newPatient) {
        var patientWithGivenEmail = patientRepository.findByEmail(newPatient.getEmail());

        if (patientWithGivenEmail.isPresent()) {
            throw new PatientAlreadyExists();
        }
        patientRepository.save(patientMapper.patientDtoToPatient(newPatient));
    }

    public PatientDto getPatientById(long id) {
        var patient = patientRepository.findById(id)
                .orElseThrow(PatientNotFoundException::new);
        return patientMapper.patientToPatientDto(patient);
    }

    @Transactional
    public void deletePatientByEmail(String email) {
        patientRepository.deleteByEmail(email);
    }

    @Transactional
    public void editPatient(String email, PatientDto newPatient) {
        var patient = patientRepository.findByEmail(email)
                .orElseThrow(PatientNotFoundException::new);

        if (!isPatientEditDataValid(newPatient, email)) {
            throw new PatientAlreadyExists();
        }

        if (!isIdCardNoEdited(patient, newPatient)) {
            throw new PatientIllegalOperationException();
        }
        patient.update(newPatient);
        patientRepository.save(patient);
    }

    @Transactional
    public void editPassword(String email, String password) {
        var editedPatient = patientRepository.findByEmail(email)
                .orElseThrow(PatientNotFoundException::new);

        if (password.equals(editedPatient.getPassword())) {
            throw new PasswordAlreadyExistsException();
        }
        editedPatient.setPassword(password);
        patientRepository.save(editedPatient);
    }

    private boolean isPatientEditDataValid(PatientDto newPatient, String email) {
        if (email.equals(newPatient.getEmail())) {
            return true;
        }

        return patientRepository.findByEmail(newPatient.getEmail()).isEmpty();
    }

    private boolean isIdCardNoEdited(Patient patient, PatientDto patientDto) {
        return patient.getIdCardNo().equals(patientDto.getIdCardNo());

    }
}
