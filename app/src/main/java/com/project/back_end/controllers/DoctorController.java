package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.DTO.Login;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final AppService appService;

    @Autowired
    public DoctorController(DoctorService doctorService, AppService appService) {
        this.doctorService = doctorService;
        this.appService = appService;
    }

    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        // Validate token
        ResponseEntity<Map<String, String>> tokenValidation = appService.validateToken(token, user);
        if (tokenValidation.getStatusCode().isError()) {
            response.put("message", tokenValidation.getBody().get("message"));
            return ResponseEntity.status(tokenValidation.getStatusCode()).body(response);
        }

        LocalDate appointmentDate = LocalDate.parse(date);
        var availability = doctorService.getDoctorAvailability(doctorId, appointmentDate);

        response.put("availability", availability);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctor() {
        Map<String, Object> response = new HashMap<>();
        var doctors = doctorService.getDoctors();
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(
            @RequestBody Doctor doctor,
            @PathVariable String token) {

        // Validate admin token
        ResponseEntity<Map<String, String>> tokenValidation = appService.validateToken(token, "admin");
        if (tokenValidation.getStatusCode().isError()) {
            return tokenValidation;
        }

        int result = doctorService.saveDoctor(doctor);

        return switch (result) {
            case 1 -> ResponseEntity.ok(Map.of("message", "Doctor added to db"));
            case -1 -> ResponseEntity.status(409).body(Map.of("message", "Doctor already exists"));
            default -> ResponseEntity.status(500).body(Map.of("message", "Some internal error occurred"));
        };
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(
            @RequestBody Doctor doctor,
            @PathVariable String token) {

        // Validate admin token
        ResponseEntity<Map<String, String>> tokenValidation = appService.validateToken(token, "admin");
        if (tokenValidation.getStatusCode().isError()) {
            return tokenValidation;
        }

        int result = doctorService.updateDoctor(doctor);

        return switch (result) {
            case 1 -> ResponseEntity.ok(Map.of("message", "Doctor updated"));
            case -1 -> ResponseEntity.status(404).body(Map.of("message", "Doctor not found"));
            default -> ResponseEntity.status(500).body(Map.of("message", "Some internal error occurred"));
        };
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(
            @PathVariable Long id,
            @PathVariable String token) {

        // Validate admin token
        ResponseEntity<Map<String, String>> tokenValidation = appService.validateToken(token, "admin");
        if (tokenValidation.getStatusCode().isError()) {
            return tokenValidation;
        }

        int result = doctorService.deleteDoctor(id);

        return switch (result) {
            case 1 -> ResponseEntity.ok(Map.of("message", "Doctor deleted successfully"));
            case -1 -> ResponseEntity.status(404).body(Map.of("message", "Doctor not found with id " + id));
            default -> ResponseEntity.status(500).body(Map.of("message", "Some internal error occurred"));
        };
    }

    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filter(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {

        // Convert "null" strings to actual null values
        String nameParam = "null".equals(name) ? null : name;
        String timeParam = "null".equals(time) ? null : time;
        String specialityParam = "null".equals(speciality) ? null : speciality;

        Map<String, Object> filteredDoctors = appService.filterDoctor(nameParam, specialityParam, timeParam);
        return ResponseEntity.ok(filteredDoctors);
    }
}
