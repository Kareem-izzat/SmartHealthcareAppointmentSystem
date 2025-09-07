package com.example.smarthealthcareappointmentsystem.DTO;
import lombok.*;

import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordDto {
    private Long patientId;
    private List<PrescriptionDto> prescriptions;
    private List<String> notes;
}
