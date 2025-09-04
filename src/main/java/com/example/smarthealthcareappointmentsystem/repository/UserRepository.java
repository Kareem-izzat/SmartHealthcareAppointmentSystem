package com.example.smarthealthcareappointmentsystem.repository;

import com.example.smarthealthcareappointmentsystem.entites.User;
import com.example.smarthealthcareappointmentsystem.entites.mongo.MedicalRecord;
import com.example.smarthealthcareappointmentsystem.entites.mongo.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
