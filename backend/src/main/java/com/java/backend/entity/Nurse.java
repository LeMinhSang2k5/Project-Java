package com.java.backend.entity;

import com.java.backend.enums.Gender;
import com.java.backend.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "nurses")
public class Nurse extends User {

    @Enumerated(EnumType.STRING)
    @Column(nullable = true) // Cho phép null
    private Gender gender;

    @Column(nullable = true) // Cho phép null
    private String phoneNumber;

    @Column(nullable = true) // Cho phép null
    private String department;

    public Nurse() {
    }

    public Nurse(String fullName, String email, String password, Gender gender, String phoneNumber, String department) {
        super(email, password, fullName, Role.SCHOOL_NURSE);
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.department = department;
    }

    public Nurse(String fullName, String email, Gender gender, String phoneNumber, String department) {
        super(email, "defaultPassword", fullName, Role.SCHOOL_NURSE); // Thay null bằng default password
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.department = department;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}