package com.java.backend.entity;

import java.time.LocalDateTime;
import com.java.backend.enums.ConsentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_campain_records")
public class StudentCampainRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private HealthCampaign campaign;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    private ConsentStatus parentConsentStatus; // Trạng thái đồng ý của phụ huynh

    private LocalDateTime consentDate;

    // One-to-one relationship to store detailed results
    @OneToOne(mappedBy = "record", cascade = CascadeType.ALL)
    private HealthCheckResult healthCheckResult;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HealthCampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(HealthCampaign campaign) {
        this.campaign = campaign;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ConsentStatus getParentConsentStatus() {
        return parentConsentStatus;
    }

    public void setParentConsentStatus(ConsentStatus parentConsentStatus) {
        this.parentConsentStatus = parentConsentStatus;
    }

    public LocalDateTime getConsentDate() {
        return consentDate;
    }

    public void setConsentDate(LocalDateTime consentDate) {
        this.consentDate = consentDate;
    }

    public HealthCheckResult getHealthCheckResult() {
        return healthCheckResult;
    }

    public void setHealthCheckResult(HealthCheckResult healthCheckResult) {
        this.healthCheckResult = healthCheckResult;
    }
}
