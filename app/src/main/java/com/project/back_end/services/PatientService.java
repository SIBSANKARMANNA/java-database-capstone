package com.project.back_end.services;

import com.project.back_end.models.Patient;
import com.project.back_end.dto.AppointmentDTO;

import com.project.back_end.repo.jpa.PatientRepository;
import com.project.back_end.repo.jpa.AppointmentRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    /* =========================
       CREATE PATIENT
    ========================= */
    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /* =========================
       GET PATIENT APPOINTMENTS
    ========================= */
    public ResponseEntity<Map<String, Object>> getPatientAppointment(
            Long id, String token) {

        String email = tokenService.extractIdentifier(token);
        Patient patient = patientRepository.findByEmail(email);

        if (patient == null || !patient.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<AppointmentDTO> dtos =
                appointmentRepository.findByPatientId(id)
                        .stream()
                        .map(AppointmentDTO::new)
                        .toList();

        return ResponseEntity.ok(Map.of("appointments", dtos));
    }

    /* =========================
       FILTER BY CONDITION
    ========================= */
    public ResponseEntity<Map<String, Object>> filterByCondition(
            String condition, Long id) {

        int status = condition.equalsIgnoreCase("past") ? 1 : 0;

        List<AppointmentDTO> dtos =
                appointmentRepository
                        .findByPatient_IdAndStatusOrderByAppointmentTimeAsc(id, status)
                        .stream()
                        .map(AppointmentDTO::new)
                        .toList();

        return ResponseEntity.ok(Map.of("appointments", dtos));
    }

    /* =========================
       FILTER BY DOCTOR
    ========================= */
    public ResponseEntity<Map<String, Object>> filterByDoctor(
            String name, Long id) {

        List<AppointmentDTO> dtos =
                appointmentRepository
                        .filterByDoctorNameAndPatientId(name, id)
                        .stream()
                        .map(AppointmentDTO::new)
                        .toList();

        return ResponseEntity.ok(Map.of("appointments", dtos));
    }

    /* =========================
       FILTER BY DOCTOR + CONDITION
    ========================= */
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(
            String condition, String name, long id) {

        int status = condition.equalsIgnoreCase("past") ? 1 : 0;

        List<AppointmentDTO> dtos =
                appointmentRepository
                        .filterByDoctorNameAndPatientIdAndStatus(name, id, status)
                        .stream()
                        .map(AppointmentDTO::new)
                        .toList();

        return ResponseEntity.ok(Map.of("appointments", dtos));
    }

    /* =========================
       PATIENT DETAILS
    ========================= */
    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {

        String email = tokenService.extractIdentifier(token);
        Patient patient = patientRepository.findByEmail(email);

        if (patient == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(Map.of("patient", patient));
    }
}
