package com.example.smarthealthcareappointmentsystem.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class Doctor extends User {

    public Doctor() {
        super.setRole(Role.DOCTOR);
    }

    private String specialty;
    private int yearsOfExperience;
}