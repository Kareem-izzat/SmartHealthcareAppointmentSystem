package com.example.smarthealthcareappointmentsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SmartHealthcareAppointmentSystemApplication {

    public static void main(String[] args) {

        SpringApplication.run(SmartHealthcareAppointmentSystemApplication.class, args);
    }

}
