package com.java.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "health_check_result")
public class HealthCheckResult {

    @Id
    private Long recordId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "record_id")
    private StudentCampainRecord record;

    private Double heightCm;
    private Double weightKg;
    private String visionLeft; // "10/10"
    private String visionRight;
    private String hearingStatus; // "Tốt", "Kém"
    private String dentalStatus; // Tình trạng răng miệng
    private String scoliosisStatus; // Tình trạng cong vẹo cột sống

    @Column(columnDefinition = "TEXT")
    private String nurseNotes;

    private boolean followUpRequired;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public StudentCampainRecord getRecord() {
        return record;
    }

    public void setRecord(StudentCampainRecord record) {
        this.record = record;
    }

    public Double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Double heightCm) {
        this.heightCm = heightCm;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public String getVisionLeft() {
        return visionLeft;
    }

    public void setVisionLeft(String visionLeft) {
        this.visionLeft = visionLeft;
    }

    public String getVisionRight() {
        return visionRight;
    }

    public void setVisionRight(String visionRight) {
        this.visionRight = visionRight;
    }

    public String getHearingStatus() {
        return hearingStatus;
    }

    public void setHearingStatus(String hearingStatus) {
        this.hearingStatus = hearingStatus;
    }

    public String getDentalStatus() {
        return dentalStatus;
    }

    public void setDentalStatus(String dentalStatus) {
        this.dentalStatus = dentalStatus;
    }

    public String getScoliosisStatus() {
        return scoliosisStatus;
    }

    public void setScoliosisStatus(String scoliosisStatus) {
        this.scoliosisStatus = scoliosisStatus;
    }

    public String getNurseNotes() {
        return nurseNotes;
    }

    public void setNurseNotes(String nurseNotes) {
        this.nurseNotes = nurseNotes;
    }

    public boolean isFollowUpRequired() {
        return followUpRequired;
    }

    public void setFollowUpRequired(boolean followUpRequired) {
        this.followUpRequired = followUpRequired;
    }

}
