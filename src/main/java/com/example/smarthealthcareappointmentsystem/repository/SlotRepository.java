package com.example.smarthealthcareappointmentsystem.repository;

import com.example.smarthealthcareappointmentsystem.entites.Slot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot,Long> {
    Page<Slot> findByDoctorId(Long doctorId, Pageable pageable);
    Page<Slot> findByDoctorIdAndAvailableTrue(Long doctorId, Pageable pageable);

    // this is a custom query to check if there are overlapping slots to prevent
    //dr from making overlapped slots
    @Query("SELECT s FROM Slot s WHERE s.doctor.id = :doctorId " +
            "AND ((:startTime < s.endTime AND :endTime > s.startTime))")
    List<Slot> findOverlappingSlots(@Param("doctorId") Long doctorId,
                                    @Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime);
}
