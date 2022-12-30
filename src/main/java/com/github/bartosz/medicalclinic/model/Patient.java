package com.github.bartosz.medicalclinic.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.bartosz.medicalclinic.exception.PatientIllegalOperationException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Builder
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

    @Setter
    private String email;
    @Setter
    private String password;
    private Long idCardNo;
    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    private String phoneNumber;
    @Setter
    private LocalDate birthday;

    public void setIdCardNo(Long idCardNo) {
        if (!this.idCardNo.equals(idCardNo)) {
            throw new PatientIllegalOperationException();
        }
    }

    public boolean isPatientValid() {
        return this.email != null && this.password != null
                && this.idCardNo != null && this.firstName != null
                && this.lastName != null && this.phoneNumber != null
                && this.birthday != null;
    }
}
