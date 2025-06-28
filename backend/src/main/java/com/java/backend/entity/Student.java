package com.java.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;


import com.java.backend.enums.Gender;
import com.java.backend.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Student extends User {

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "student_class", nullable = false)
    private String studentClass;

    @Column(name = "city")
    private String city;

    @Column(name = "district")
    private String district;

    @Column(name = "grade")
    private String grade;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true)
    private Parent parent;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonManagedReference
    private HealthProfile healthProfile;

    public Student() {
        super();
    }

    public Student(String fullName, String email, String password, String code, LocalDate dateOfBirth, Gender gender,
            String studentClass) {
        super(email, password, fullName, Role.STUDENT);
        this.code = code;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.studentClass = studentClass;
    }

    // Constructor với password mặc định cho import Excel
    public Student(String fullName, String email, String code, LocalDate dateOfBirth, Gender gender,
            String studentClass) {
        super(email, "defaultPassword123", fullName, Role.STUDENT);
        this.code = code;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.studentClass = studentClass;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public HealthProfile getHealthProfile() {
        return healthProfile;
    }

    public void setHealthProfile(HealthProfile healthProfile) {
        this.healthProfile = healthProfile;
        if (healthProfile != null) {
            healthProfile.setStudent(this);
        }
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

}