package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppService appService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, AppService appService) {
        this.appointmentService = appointmentService;
        this.appService = appService;
    }

    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(
            @PathVariable String date,
            @PathVariable String patientName,
            @PathVariable String token) {

        // Validate doctor token
        ResponseEntity<Map<String, String>> tokenValidation = appService.validateToken(token, "doctor");
        if (tokenValidation.getStatusCode().isError()) {
            return ResponseEntity
                    .status(tokenValidation.getStatusCode())
                    .body(Map.of("message", tokenValidation.getBody().get("message")));
        }

        LocalDate appointmentDate = LocalDate.parse(date);
        String patientNameParam = "null".equals(patientName) ? null : patientName;

        Map<String, Object> appointments =
                appointmentService.getAppointment(patientNameParam, appointmentDate, token);

        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(
            @RequestBody Appointment appointment,
            @PathVariable String token) {

        // Validate patient token
        ResponseEntity<Map<String, String>> tokenValidation = appService.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().isError()) {
            return tokenValidation;
        }

        // Validate appointment using AppService
        int validationResult = appService.validateAppointment(appointment);
        if (validationResult == -1) {
            return ResponseEntity.badRequest().body(Map.of("message", "Doctor does not exist"));
        } else if (validationResult == 0) {
            return ResponseEntity.badRequest().body(Map.of("message", "Appointment time is not available"));
        }

        // Book appointment
        int bookingResult = appointmentService.bookAppointment(appointment);
        if (bookingResult == 1) {
            return ResponseEntity.status(201).body(Map.of("message", "Appointment booked successfully"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to book appointment"));
        }
    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(
            @RequestBody Appointment appointment,
            @PathVariable String token) {

        // Validate patient token
        ResponseEntity<Map<String, String>> tokenValidation = appService.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().isError()) {
            return tokenValidation;
        }

        return appointmentService.updateAppointment(appointment);
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(
            @PathVariable long id,
            @PathVariable String token) {

        // Validate patient token
        ResponseEntity<Map<String, String>> tokenValidation = appService.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().isError()) {
            return tokenValidation;
        }

        return appointmentService.cancelAppointment(id, token);
    }
}
