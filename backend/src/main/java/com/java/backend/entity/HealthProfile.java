package com.java.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "health_profiles")
public class HealthProfile {
    @Id
    private Long id; // Sử dụng chung ID với Student

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Đánh dấu rằng trường này là một phần của khóa chính và cũng là khóa ngoại
    @JoinColumn(name = "id")
    private Student student;

    @Lob // Dành cho các chuỗi văn bản dài
    private String allergies; // Ví dụ: "Hải sản, phấn hoa"

    @Lob
    private String chronicDiseases; // Ví dụ: "Hen suyễn"

    @Lob
    private String medicalHistory; // Tiền sử bệnh án

    @Lob
    private String vaccinationHistory; // Lịch sử tiêm chủng

    private String visionDetails; // Ví dụ: "Mắt trái: 8/10, Mắt phải: 7/10"
    private String hearingDetails; // Ví dụ: "Bình thường"

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getChronicDiseases() {
        return chronicDiseases;
    }

    public void setChronicDiseases(String chronicDiseases) {
        this.chronicDiseases = chronicDiseases;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getVaccinationHistory() {
        return vaccinationHistory;
    }

    public void setVaccinationHistory(String vaccinationHistory) {
        this.vaccinationHistory = vaccinationHistory;
    }

    public String getVisionDetails() {
        return visionDetails;
    }

    public void setVisionDetails(String visionDetails) {
        this.visionDetails = visionDetails;
    }

    public String getHearingDetails() {
        return hearingDetails;
    }

    public void setHearingDetails(String hearingDetails) {
        this.hearingDetails = hearingDetails;
    }
}