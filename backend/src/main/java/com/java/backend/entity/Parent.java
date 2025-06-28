package com.java.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.backend.enums.Gender;
import com.java.backend.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "parents")
public class Parent extends User {
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore // Tránh vòng lặp khi trả JSON
    private List<Student> students;

    public Parent() {
    }

    public Parent(String fullName, String email, String password, Gender gender, String phoneNumber) {
        super(email, password, fullName, Role.PARENT);
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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
