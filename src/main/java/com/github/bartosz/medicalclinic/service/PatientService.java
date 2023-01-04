package com.github.bartosz.medicalclinic.service;

import com.github.bartosz.medicalclinic.dto.PatientDto;
import com.github.bartosz.medicalclinic.exception.PasswordAlreadyExistsException;
import com.github.bartosz.medicalclinic.exception.PatientAlreadyExists;
import com.github.bartosz.medicalclinic.exception.PatientIllegalOperationException;
import com.github.bartosz.medicalclinic.exception.PatientNotFoundException;
import com.github.bartosz.medicalclinic.model.Patient;
import com.github.bartosz.medicalclinic.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patient -> modelMapper.map(patient, PatientDto.class))
                .collect(Collectors.toList());
    }

    public PatientDto getPatientByEmail(String email) {
        var patient = patientRepository.findByEmail(email)
                .orElseThrow(PatientNotFoundException::new);
        return modelMapper.map(patient, PatientDto.class);
    }

    public void addPatient(PatientDto newPatient) {
        var patientWithGivenEmail = patientRepository.findByEmail(newPatient.getEmail());

        if (patientWithGivenEmail.isPresent()) {
            throw new PatientAlreadyExists();
        }

        patientRepository.save(modelMapper.map(newPatient,Patient.class));
    }

    public PatientDto getPatientById(long id) {
        var patient = patientRepository.getPatientById(id)
                .orElseThrow(PatientNotFoundException::new);
        return modelMapper.map(patient, PatientDto.class);
    }

    public void deletePatientByEmail(String email) {
        patientRepository.deleteByEmail(email);
    }

    public void editPatient(String email, PatientDto newPatient) {
        if (!isPatientEditDataValid(newPatient, email)) {
            throw new PatientAlreadyExists();
        }

        if(!isIdCardNoEdited(newPatient)){
            throw new PatientIllegalOperationException();
        }

        patientRepository.update(email, modelMapper.map(newPatient, Patient.class));
    }

    public void editPassword(String email, String password) {
        var editedPatient = patientRepository.findByEmail(email)
                .orElseThrow(PatientNotFoundException::new);

        if (password.equals(editedPatient.getPassword())) {
            throw new PasswordAlreadyExistsException();
        }

        editedPatient.setPassword(password);

        patientRepository.update(email, editedPatient);
    }

    private boolean isPatientEditDataValid(PatientDto newPatient, String email) {
        if (email.equals(newPatient.getEmail())) {
            return true;
        }

        return patientRepository.findByEmail(newPatient.getEmail()).isEmpty();
    }

    private boolean isIdCardNoEdited(PatientDto patientDto){
        var entity = patientRepository.findByEmail(patientDto.getEmail());

        return entity.map(patient -> patientDto.getIdCardNo().equals(patient.getIdCardNo())).orElse(true);

    }
}
