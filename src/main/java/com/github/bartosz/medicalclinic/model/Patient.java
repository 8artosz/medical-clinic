package com.github.bartosz.medicalclinic.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
public class Patient {
    @JsonCreator
    public Patient(String email, String password, Long idCardNo, String firstName, String lastName, String phoneNumber, LocalDate birthday) {
        this.email = email;
        this.password = password;
        this.idCardNo = idCardNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }


    private String email;
    private String password;
    private Long idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;

    public boolean isPatientValid() {
        return this.email != null && this.password != null
                && this.idCardNo != null && this.firstName != null
                && this.lastName != null && this.phoneNumber != null
                && this.birthday != null;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(email, patient.email) && Objects.equals(password, patient.password) && Objects.equals(idCardNo, patient.idCardNo) && Objects.equals(firstName, patient.firstName) && Objects.equals(lastName, patient.lastName) && Objects.equals(phoneNumber, patient.phoneNumber) && Objects.equals(birthday, patient.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, idCardNo, firstName, lastName, phoneNumber, birthday);
    }
}
