package com.java.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "health_profiles")
public class HealthProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "class_name")
    private String className;

    @Column(name = "grade")
    private String grade;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "city")
    private String city;

    @Column(name = "district")
    private String district;

    @Lob
    @Column(name = "allergies")
    private String allergies;

    @Lob
    @Column(name = "chronic_diseases")
    private String chronicDiseases;

    @Lob
    @Column(name = "medical_history")
    private String medicalHistory;

    @Lob
    @Column(name = "vaccination_history")
    private String vaccinationHistory;

    @Column(name = "vision_details")
    private String visionDetails;

    @Column(name = "hearing_details")
    private String hearingDetails;


    // Getters and Setters
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}