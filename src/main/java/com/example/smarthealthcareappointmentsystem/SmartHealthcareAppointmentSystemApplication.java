package com.example.smarthealthcareappointmentsystem;

import com.example.smarthealthcareappointmentsystem.entites.Admin;
import com.example.smarthealthcareappointmentsystem.entites.Role;
import com.example.smarthealthcareappointmentsystem.entites.User;
import com.example.smarthealthcareappointmentsystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories
@EnableMongoRepositories
public class SmartHealthcareAppointmentSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartHealthcareAppointmentSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
                User admin = new Admin();
                admin.setEmail("admin@admin.com");
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setPhone("0000000000");
                admin.setRole(Role.ADMIN);

                // encode the password
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                admin.setPassword(encoder.encode("Admin123!"));

                userRepository.save(admin);
                System.out.println("Admin user created: admin@admin.com / Admin123!");
            }
        };
    }
}
