package com.example.smarthealthcareappointmentsystem.DTO;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String specialty;
    private int yearsOfExperience;
    private List<SlotDto> slots;
}
