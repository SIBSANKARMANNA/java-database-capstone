


package com.project.back_end.controllers;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.back_end.services.TokenValidationService;



@Controller
public class DashboardController {

    @Autowired
    private TokenValidationService tokenValidationService;

    /**
     * Admin Dashboard View
     */
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {

        Map<String, String> validationResult =
                tokenValidationService.validateToken(token, "admin");

        // If map is empty → token is valid
        if (validationResult.isEmpty()) {
            return "admin/adminDashboard";
        }

        // Token invalid → redirect to login
        return "redirect:/";
    }

    /**
     * Doctor Dashboard View
     */
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {

        Map<String, String> validationResult =
                tokenValidationService.validateToken(token, "doctor");

        if (validationResult.isEmpty()) {
            return "doctor/doctorDashboard";
        }

        return "redirect:/";
    }
}
