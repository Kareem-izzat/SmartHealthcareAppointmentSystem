package com.example.smarthealthcareappointmentsystem.repository;

import com.example.smarthealthcareappointmentsystem.entites.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findBySlot_Doctor_Id(Long doctorId);

        // doctor id will be acceses from slot
        Optional<Appointment> findByIdAndSlot_DoctorId(Long appointmentId, Long doctorId);
    List<Appointment> findByPatientId(Long patientId);

}
