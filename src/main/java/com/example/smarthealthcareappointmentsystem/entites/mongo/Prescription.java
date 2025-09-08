package com.example.smarthealthcareappointmentsystem.entites.mongo;

import jakarta.persistence.Cacheable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.Id;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "prescriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Prescription {
    @Id
    private String id;
    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private Long appointmentId;
    private LocalDateTime date;

    private List<String> medicines;
    private String notes;
}
