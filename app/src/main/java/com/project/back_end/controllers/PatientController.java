package com.project.back_end.controllers;

import com.project.back_end.models.Patient;
import com.project.back_end.DTO.Login;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final AppService service;

    @Autowired
    public PatientController(PatientService patientService, AppService service) {
        this.patientService = patientService;
        this.service = service;
    }

    @GetMapping("/{token}")
    public ResponseEntity<Map<String, Object>> getPatient(@PathVariable String token) {
        // Validate patient token
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().isError()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", tokenValidation.getBody().get("message"));
            return ResponseEntity.status(tokenValidation.getStatusCode()).body(response);
        }

        return patientService.getPatientDetails(token);
    }

    @PostMapping()
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        Map<String, String> response = new HashMap<>();
        
        // Validate if patient already exists
        boolean isValidPatient = service.validatePatient(patient);
        if (!isValidPatient) {
            response.put("message", "Patient with email id or phone no already exist");
            return ResponseEntity.status(409).body(response);
        }

        // Create patient
        int result = patientService.createPatient(patient);
        if (result == 1) {
            response.put("message", "Signup successful");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Internal server error");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login login) {
        return service.validatePatientLogin(login);
    }

    @GetMapping("/{id}/{token}")
    public ResponseEntity<Map<String, Object>> getPatientAppointment(
            @PathVariable Long id,
            @PathVariable String token) {
        
        // Validate patient token
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().isError()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", tokenValidation.getBody().get("message"));
            return ResponseEntity.status(tokenValidation.getStatusCode()).body(response);
        }

        return patientService.getPatientAppointment(id, token);
    }

    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterPatientAppointment(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {
        
        // Validate patient token
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().isError()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", tokenValidation.getBody().get("message"));
            return ResponseEntity.status(tokenValidation.getStatusCode()).body(response);
        }

        // Convert "null" strings to actual null values
        String conditionParam = "null".equals(condition) ? null : condition;
        String nameParam = "null".equals(name) ? null : name;
        
        return service.filterPatient(conditionParam, nameParam, token);
    }
}