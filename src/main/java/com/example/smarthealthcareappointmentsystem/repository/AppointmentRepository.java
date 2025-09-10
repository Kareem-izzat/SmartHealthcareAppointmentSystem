package com.example.smarthealthcareappointmentsystem.repository;

import com.example.smarthealthcareappointmentsystem.entites.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    Page<Appointment> findBySlot_Doctor_Id(Long doctorId, Pageable pageable);
    Page<Appointment> findByPatientId(Long patientId, Pageable pageable);
        // doctor id will be acceses from slot
     Optional<Appointment> findByIdAndSlot_DoctorId(Long appointmentId, Long doctorId);
    boolean existsByPatientIdAndSlotStartTime(Long patientId, LocalDateTime startTime);
    Optional<Appointment> findBySlotId(Long slotId);
    List<Appointment> findBySlot_Doctor_IdAndSlot_StartTimeAfter(Long doctorId, LocalDateTime time);



}
