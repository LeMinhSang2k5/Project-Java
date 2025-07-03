package com.java.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_checkup_notifications")
public class MedicalCheckupNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "parent_consent_date")
    private LocalDateTime parentConsentDate;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getParentConsentDate() {
        return parentConsentDate;
    }

    public void setParentConsentDate(LocalDateTime parentConsentDate) {
        this.parentConsentDate = parentConsentDate;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Long getId() {
        return id;
    }

}