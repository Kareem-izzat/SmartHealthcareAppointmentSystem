package com.example.smarthealthcareappointmentsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SmartHealthcareAppointmentSystemApplication {

    public static void main(String[] args) {
        System.out.println("hi there");
        String rawPassword = "Admin123!";
        String encoded = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println(encoded);
        SpringApplication.run(SmartHealthcareAppointmentSystemApplication.class, args);
    }

}
