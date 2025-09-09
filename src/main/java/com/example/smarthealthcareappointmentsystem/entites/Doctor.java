package com.example.smarthealthcareappointmentsystem.entites;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

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

    @OneToMany(
            mappedBy = "doctor",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Slot> slots;
}
