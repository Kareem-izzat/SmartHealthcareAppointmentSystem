package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.DoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.PatientDto;
import com.example.smarthealthcareappointmentsystem.entites.Doctor;
import com.example.smarthealthcareappointmentsystem.entites.Patient;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.mapper.DoctorMapper;
import com.example.smarthealthcareappointmentsystem.mapper.PatientMapper;
import com.example.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.example.smarthealthcareappointmentsystem.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorMapper doctorMapper;
    private final PatientMapper patientMapper;
   @Override
    public List<DoctorDto> getAllDoctors() {
       return doctorRepository.findAll().stream()
               .map(doctorMapper::toDto)
               .collect(Collectors.toList());
   }
    @Override
    public DoctorDto updateDoctorById(Long doctorId, DoctorDto doctorDto){
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + doctorId));
        doctor.setFirstName(doctorDto.getFirstName());
        doctor.setLastName(doctorDto.getLastName());
        doctor.setEmail(doctorDto.getEmail());
        doctor.setPhone(doctorDto.getPhone());
        doctor.setSpecialty(doctorDto.getSpecialty());
        doctor.setYearsOfExperience(doctorDto.getYearsOfExperience());
        Doctor updatedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(updatedDoctor);

    }
    @Override
    public DoctorDto getDoctorById(Long doctorId) {
       Doctor doctor=doctorRepository.findById(doctorId)
               .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + doctorId));
       return doctorMapper.toDto(doctor);
    }
    @Override
    public DoctorDto addDoctor(DoctorDto doctorDto) {
        Doctor doctor = doctorMapper.toEntity(doctorDto);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(savedDoctor);
    }
    @Override
    public void RemoveDoctorById(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id " + doctorId);
        }
        doctorRepository.deleteById(doctorId);
    }

    @Override
    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public PatientDto updatePatient(Long patientId, PatientDto patientDto){
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + patientId));
        patient.setFirstName(patientDto.getFirstName());
        patient.setLastName(patientDto.getLastName());
        patient.setEmail(patientDto.getEmail());
        patient.setPhone(patientDto.getPhone());
        patient.setDateOfBirth(patientDto.getDateOfBirth());
        patient.setMedicalHistory(patientDto.getMedicalHistory());
        Patient updatedPatient = patientRepository.save(patient);
        return patientMapper.toDto(updatedPatient);

    }
    @Override
    public PatientDto getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + patientId));
        return patientMapper.toDto(patient);
    }
    @Override
    public PatientDto addPatient(PatientDto patientDto) {
        Patient patient = patientMapper.toEntity(patientDto);
        Patient saved = patientRepository.save(patient);
        return patientMapper.toDto(saved);
    }

    @Override
    public void removePatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id " + patientId);
        }
        patientRepository.deleteById(patientId);
    }



}
