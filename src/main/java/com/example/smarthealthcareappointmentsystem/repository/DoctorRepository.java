package com.example.smarthealthcareappointmentsystem.repository;

import com.example.smarthealthcareappointmentsystem.entites.Doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    Page<Doctor> findBySpecialtyIgnoreCase(String specialty, Pageable pageable);
    boolean existsByEmail (String email);

}
