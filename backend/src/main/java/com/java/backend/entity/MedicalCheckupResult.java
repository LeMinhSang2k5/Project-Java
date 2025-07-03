package com.java.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_checkup_results")
public class MedicalCheckupResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "checkup_date", nullable = false)
    private LocalDateTime checkupDate;

    @Column(name = "result", columnDefinition = "TEXT")
    private String result;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "abnormal", nullable = false)
    private boolean abnormal;

    @Column(name = "created_by")
    private Long createdBy; // nurseId

    @Column(name = "student_confirmation")
    private Boolean studentConfirmation = false;

    @Column(name = "student_confirmation_date")
    private LocalDateTime studentConfirmationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public LocalDateTime getCheckupDate() {
        return checkupDate;
    }

    public void setCheckupDate(LocalDateTime checkupDate) {
        this.checkupDate = checkupDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isAbnormal() {
        return abnormal;
    }

    public void setAbnormal(boolean abnormal) {
        this.abnormal = abnormal;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getStudentConfirmation() {
        return studentConfirmation;
    }

    public void setStudentConfirmation(Boolean studentConfirmation) {
        this.studentConfirmation = studentConfirmation;
    }

    public LocalDateTime getStudentConfirmationDate() {
        return studentConfirmationDate;
    }

    public void setStudentConfirmationDate(LocalDateTime studentConfirmationDate) {
        this.studentConfirmationDate = studentConfirmationDate;
    }

}