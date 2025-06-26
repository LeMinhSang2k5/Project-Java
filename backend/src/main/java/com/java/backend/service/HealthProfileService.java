package com.java.backend.service;

import com.java.backend.entity.HealthProfile;
import com.java.backend.entity.Student;
import com.java.backend.repository.HealthProfileRepository;
import com.java.backend.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HealthProfileService {

    @Autowired
    private HealthProfileRepository healthProfileRepository;

    @Autowired
    private StudentRepository studentRepository;

    public HealthProfile saveHealthProfile(HealthProfile healthProfile) {
        if (healthProfile.getStudent() == null || healthProfile.getStudent().getId() == null) {
            throw new RuntimeException("Student ID must be provided when creating HealthProfile.");
        }

        Long studentId = healthProfile.getStudent().getId();

        // Tìm student từ database
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        // Gán đúng quan hệ hai chiều
        healthProfile.setStudent(student);
        student.setHealthProfile(healthProfile);

        return healthProfileRepository.save(healthProfile);
    }

    public List<HealthProfile> getAllHealthProfiles() {
        return healthProfileRepository.findAll();
    }

    public HealthProfile getHealthProfileById(Long id) {
        return healthProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Health profile not found with id: " + id));
    }

    public HealthProfile updateHealthProfile(HealthProfile healthProfile) {
        return healthProfileRepository.save(healthProfile);
    }

    public HealthProfile findByStudentId(Long studentId) {
        return healthProfileRepository.findByStudentId(studentId).orElse(null);
    }

    public void deleteHealthProfile(Long id) {
        healthProfileRepository.deleteById(id);
    }
}
