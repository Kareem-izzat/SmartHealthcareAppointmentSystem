package com.example.smarthealthcareappointmentsystem.repository;

import com.example.smarthealthcareappointmentsystem.entites.Doctor;
import com.example.smarthealthcareappointmentsystem.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    List<Doctor> findBySpecialty(String specialty);
    boolean existsByEmail (String email);
    Optional<Doctor> findByEmail(String email);
}
