package com.example.smarthealthcareappointmentsystem.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "admins")
@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class Admin extends User {
    public Admin() {
        super.setRole(Role.ADMIN);
    }


    private String department;
}