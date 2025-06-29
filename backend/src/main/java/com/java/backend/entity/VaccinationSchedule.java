package com.java.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.java.backend.enums.ConsentStatus;

@Entity
@Table(name = "vaccination_schedules")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VaccinationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "student_code")
    private String studentCode;

    @Column(name = "vaccine_name", nullable = false)
    private String vaccineName;

    @Column(name = "scheduled_date_time", nullable = false)
    private LocalDateTime scheduledDateTime;

    @Column(name = "is_vaccinated", nullable = false)
    private boolean isVaccinated = false;

    @Column(name = "actual_vaccination_date")
    private LocalDateTime actualVaccinationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "parent_consent")
    private ConsentStatus parentConsent = ConsentStatus.PENDING;

    @Column(name = "parent_consent_date")
    private LocalDateTime parentConsentDate;

    @Column(name = "student_confirmation")
    private boolean studentConfirmation = false;

    @Column(name = "student_confirmation_date")
    private LocalDateTime studentConfirmationDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "location")
    private String location;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    // Constructors
    public VaccinationSchedule() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    public VaccinationSchedule(String studentName, String vaccineName, LocalDateTime scheduledDateTime) {
        this();
        this.studentName = studentName;
        this.vaccineName = vaccineName;
        this.scheduledDateTime = scheduledDateTime;
    }

    // Getters and Setters
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public boolean isVaccinated() {
        return isVaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        isVaccinated = vaccinated;
        if (vaccinated && this.actualVaccinationDate == null) {
            this.actualVaccinationDate = LocalDateTime.now();
        }
    }

    public LocalDateTime getActualVaccinationDate() {
        return actualVaccinationDate;
    }

    public void setActualVaccinationDate(LocalDateTime actualVaccinationDate) {
        this.actualVaccinationDate = actualVaccinationDate;
    }

    public ConsentStatus getParentConsent() {
        return parentConsent;
    }

    public void setParentConsent(ConsentStatus parentConsent) {
        this.parentConsent = parentConsent;
        if (parentConsent != ConsentStatus.PENDING) {
            this.parentConsentDate = LocalDateTime.now();
        }
    }

    public LocalDateTime getParentConsentDate() {
        return parentConsentDate;
    }

    public void setParentConsentDate(LocalDateTime parentConsentDate) {
        this.parentConsentDate = parentConsentDate;
    }

    public boolean isStudentConfirmation() {
        return studentConfirmation;
    }

    public void setStudentConfirmation(boolean studentConfirmation) {
        this.studentConfirmation = studentConfirmation;
        if (studentConfirmation) {
            this.studentConfirmationDate = LocalDateTime.now();
        }
    }

    public LocalDateTime getStudentConfirmationDate() {
        return studentConfirmationDate;
    }

    public void setStudentConfirmationDate(LocalDateTime studentConfirmationDate) {
        this.studentConfirmationDate = studentConfirmationDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}
