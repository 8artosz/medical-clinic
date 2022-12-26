package com.github.bartosz.medicalclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bartosz.medicalclinic.util.PatientUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PatientControllerTest {

    @Autowired
    private PatientController patientController;

    @Autowired
    private ObjectMapper objectMapper;
    private static final String ROOT_PATH = "$";
    private static final String EMAIL_PATH = "$[0].email";

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void setup() {
       patientController.deletePatientByEmail("test@gmail.com");
    }

    @Test
    void getAllPatientsTest() throws Exception {
        var patient = PatientUtils.buildPatientDto();
        patientController.addPatient(patient);
        mockMvc.perform(get("/patients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(ROOT_PATH).isArray())
                .andExpect(jsonPath(EMAIL_PATH).value(patient.getEmail()));
    }

    @Test
    void getPatientByEmailTest() throws Exception {
        var patient = PatientUtils.buildPatientDto();
        patientController.addPatient(patient);
        mockMvc.perform(get("/patients/{email}", patient.getEmail()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(patient.getEmail()));
    }

    @Test
    void getPatientByIdTest() throws Exception {
        var patient = PatientUtils.buildPatientDto();
        patientController.addPatient(patient);
        mockMvc.perform(get("/patients/id")
                        .param("id", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(patient.getEmail()));
    }

    @Test
    void addPatientTest() throws Exception {
        var patient = PatientUtils.buildPatientDto();
        mockMvc.perform(post("/patients")
                        .content(objectMapper.writeValueAsString(patient))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void deletePatientByEmailTest() throws Exception {
        var patient = PatientUtils.buildPatientDto();
        patientController.addPatient(patient);
        mockMvc.perform(delete("/patients/{email}", patient.getEmail()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void editPatientTest() throws Exception {
        var patient = PatientUtils.buildPatientDto();
        patientController.addPatient(patient);
        mockMvc.perform(put("/patients/{email}", patient.getEmail())
                        .content(objectMapper.writeValueAsString(patient))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void editPasswordTest() throws Exception {
        var patient = PatientUtils.buildPatientDto();
        patientController.addPatient(patient);
        mockMvc.perform(patch("/patients/{email}", patient.getEmail())
                        .content(objectMapper.writeValueAsString("asd123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
