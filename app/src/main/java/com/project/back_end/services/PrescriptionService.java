package com.project.back_end.services;

import com.project.back_end.mongo.models.Prescription;
import com.project.back_end.repo.mongo.PrescriptionRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public ResponseEntity<Map<String, String>> savePrescription(
            Prescription prescription) {

        prescriptionRepository.save(prescription);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Prescription saved"));
    }

    public ResponseEntity<Map<String, Object>> getPrescription(
            Long appointmentId) {

        List<Prescription> prescriptions =
                prescriptionRepository.findByAppointmentId(appointmentId);

        return ResponseEntity.ok(
                Map.of("prescriptions", prescriptions)
        );
    }
}
