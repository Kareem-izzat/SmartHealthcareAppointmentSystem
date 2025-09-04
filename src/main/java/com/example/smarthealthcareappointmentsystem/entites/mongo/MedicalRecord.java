package com.example.smarthealthcareappointmentsystem.entites.mongo;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "medical_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {
    @Id
    private String id;

    private Long patientId;

    private List<Prescription> prescriptions;
    private List<String> notes;

}
