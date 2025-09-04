package com.example.smarthealthcareappointmentsystem.entites;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class Patient extends User {
    public Patient() {
        super.setRole(Role.PATIENT);
    }
    private LocalDate dateOfBirth;
    private String medicalHistory;
}
