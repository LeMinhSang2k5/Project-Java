package com.java.backend.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "health_incidents")
public class HealthIncident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by_user_id", nullable = false) // Y tá ghi nhận
    private User reportedBy;

    @Column(nullable = false)
    private String incidentType; // Ví dụ: "Té ngã", "Sốt cao", "Đau bụng"

    @Lob
    @Column(nullable = false)
    private String description; // Mô tả chi tiết

    @Lob
    private String actionTaken; // Hành động đã xử lý

    @Column(nullable = false)
    private LocalDateTime incidentTime;

    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    public enum IncidentStatus {
        REPORTED, MONITORING, PARENT_NOTIFIED, RESOLVED
    }
}
