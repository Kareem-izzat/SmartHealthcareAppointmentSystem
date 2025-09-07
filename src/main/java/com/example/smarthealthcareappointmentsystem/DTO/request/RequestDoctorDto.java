package com.example.smarthealthcareappointmentsystem.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDoctorDto {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6,max=20,message = "minimum size is 6 and max is 20")
    private String password;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "speciality is required")
    private String specialty;
    @NotNull(message = "years of experience are required")
    private int yearsOfExperience;
    private List<RequestSlotDto> slots;
}

