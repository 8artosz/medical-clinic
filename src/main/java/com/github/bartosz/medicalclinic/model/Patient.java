package com.github.bartosz.medicalclinic.model;

import com.github.bartosz.medicalclinic.exception.PatientIllegalOperationException;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Patient {
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
        if (this.idCardNo != null && !this.idCardNo.equals(idCardNo)) {
            throw new PatientIllegalOperationException();
        }
        this.idCardNo = idCardNo;
    }

    public boolean isPatientValid() {
        return this.email != null && this.password != null
                && this.idCardNo != null && this.firstName != null
                && this.lastName != null && this.phoneNumber != null
                && this.birthday != null;
    }
}
