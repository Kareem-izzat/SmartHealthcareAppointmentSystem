package com.example.smarthealthcareappointmentsystem.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor extends User {

    private String specialty;
    private int yearsOfExperience;
}