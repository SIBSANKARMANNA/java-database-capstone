package com.project.back_end.controllers;

import com.project.back_end.mongo.models.Prescription;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.AuthService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private AuthService authService;

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> save(
            @RequestBody Prescription prescription,
            @PathVariable String token) {

        authService.validateToken(token, "doctor");
        return prescriptionService.savePrescription(prescription);
    }

    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> get(
            @PathVariable Long appointmentId,
            @PathVariable String token) {

        authService.validateToken(token, "doctor");
        return prescriptionService.getPrescription(appointmentId);
    }
}
