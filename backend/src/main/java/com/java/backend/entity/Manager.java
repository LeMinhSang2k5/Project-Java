package com.java.backend.entity;

import com.java.backend.enums.Gender;
import com.java.backend.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "managers")
public class Manager extends User {

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String phoneNumber;

    public Manager() {
    }

    public Manager(String fullName, String email, Gender gender, String phoneNumber) {
        super(email, null, fullName, Role.MANAGER);
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}