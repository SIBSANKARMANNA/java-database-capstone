


package com.project.back_end.controllers;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.back_end.services.TokenService;



@Controller
public class DashboardController {

    @Autowired
    private TokenService tokenService;

    /**
     * Admin Dashboard View
     */
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {


        boolean isValid = tokenService.validateToken(token, "admin");
        if (isValid) {
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

        // Map<String, String> validationResult =
        //         tokenService.validateToken(token, "doctor");

        // if (validationResult.isEmpty()) {
        //     return "doctor/doctorDashboard";
        // }

        boolean isValid = tokenService.validateToken(token, "doctor");
        if (isValid) {
            return "doctor/doctorDashboard";
        }


        return "redirect:/";
    }
}
