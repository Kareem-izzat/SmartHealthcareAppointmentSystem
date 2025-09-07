package com.example.smarthealthcareappointmentsystem.DTO;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private String role;
}
