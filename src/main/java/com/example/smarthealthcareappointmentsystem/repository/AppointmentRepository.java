package com.example.smarthealthcareappointmentsystem.repository;

import com.example.smarthealthcareappointmentsystem.entites.Appointment;
import com.example.smarthealthcareappointmentsystem.entites.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByDoctorId(Long doctorId);
    Optional<Appointment> findByIdAndDoctorId(Long AppointmentId,Long doctorId);

}
