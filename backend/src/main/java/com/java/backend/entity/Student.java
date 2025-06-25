package com.java.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.java.backend.enums.Gender;
import com.java.backend.enums.Role;

@Entity
@Table(name = "students")
public class Student extends User {

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String studentClass;

    public Student() {
    }

    public Student(String fullName, String email, LocalDate dateOfBirth, Gender gender, String studentClass) {
        super(email, null, fullName, Role.STUDENT);
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.studentClass = studentClass;
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