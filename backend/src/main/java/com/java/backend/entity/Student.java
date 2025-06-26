package com.java.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.java.backend.enums.Gender;
import com.java.backend.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Student extends User {

    @Column(name = "code", unique = true, nullable = false)
    private String code; // Mã số sinh viên

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "student_class", nullable = false)
    private String studentClass;

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
        super(email, "defaultPassword123", fullName, Role.STUDENT); // Password mặc định
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
}