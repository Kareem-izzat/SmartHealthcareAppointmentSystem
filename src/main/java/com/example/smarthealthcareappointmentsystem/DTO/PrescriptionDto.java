package com.example.smarthealthcareappointmentsystem.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDto {
    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private Long appointmentId;
    private LocalDateTime date;

    private List<String> medicines;
    private String notes;
}
