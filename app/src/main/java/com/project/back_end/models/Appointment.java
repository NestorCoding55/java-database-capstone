package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull(message = "Doctor cannot be null")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "Patient cannot be null")
    private Patient patient;

    @Column(name = "appointment_time", nullable = false)
    @NotNull(message = "Appointment time cannot be null")
    @Future(message = "Appointment time must be in the future")
    private LocalDateTime appointmentTime;

    @Column(nullable = false)
    @NotNull(message = "Status cannot be null")
    private Integer status; // 0 = scheduled, 1 = completed

    // Default constructor (required by JPA)
    public Appointment() {
    }

    // Parameterized constructor
    public Appointment(Doctor doctor, Patient patient, LocalDateTime appointmentTime, Integer status) {
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    // Transient method - not persisted in database
    @Transient
    public LocalDateTime getEndTime() {
        return this.appointmentTime != null ? this.appointmentTime.plusHours(1) : null;
    }

    // Transient method - not persisted in database
    @Transient
    public LocalDate getAppointmentDate() {
        return this.appointmentTime != null ? this.appointmentTime.toLocalDate() : null;
    }

    // Transient method - not persisted in database
    @Transient
    public LocalTime getAppointmentTimeOnly() {
        return this.appointmentTime != null ? this.appointmentTime.toLocalTime() : null;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    // Helper methods for status
    @Transient
    public boolean isScheduled() {
        return status != null && status == 0;
    }

    @Transient
    public boolean isCompleted() {
        return status != null && status == 1;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", doctor=" + (doctor != null ? doctor.getId() : "null") +
                ", patient=" + (patient != null ? patient.getId() : "null") +
                ", appointmentTime=" + appointmentTime +
                ", status=" + status +
                '}';
    }
}