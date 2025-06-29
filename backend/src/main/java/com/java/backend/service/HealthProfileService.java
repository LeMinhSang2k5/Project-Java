package com.java.backend.service;

import com.java.backend.entity.HealthProfile;
import com.java.backend.entity.Student;
import com.java.backend.repository.HealthProfileRepository;
import com.java.backend.repository.StudentRepository;
import com.java.backend.enums.Gender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class HealthProfileService {

    @Autowired
    private HealthProfileRepository healthProfileRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public HealthProfile saveHealthProfile(HealthProfile healthProfile) {
        if (healthProfile.getStudent() == null || healthProfile.getStudent().getId() == null) {
            throw new RuntimeException("Student ID must be provided when creating HealthProfile.");
        }

        Long studentId = healthProfile.getStudent().getId();

        // Tìm student từ database
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        // Đồng bộ thông tin từ HealthProfile vào Student
        syncStudentInfoFromHealthProfile(student, healthProfile);
        
        // Lưu student trước
        studentRepository.save(student);

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

    @Transactional
    public HealthProfile updateHealthProfile(HealthProfile healthProfile) {
        try {
            System.out.println("Starting update for health profile ID: " + healthProfile.getId());
            
            if (!healthProfileRepository.existsById(healthProfile.getId())) {
                throw new RuntimeException("Health profile not found with id: " + healthProfile.getId());
            }
            
            // Lấy health profile hiện tại từ database
            HealthProfile existingProfile = healthProfileRepository.findById(healthProfile.getId())
                    .orElseThrow(() -> new RuntimeException("Health profile not found with id: " + healthProfile.getId()));
            
            System.out.println("Found existing profile: " + existingProfile.getId());
            
            // Cập nhật các field với null check
            if (healthProfile.getStudentName() != null) {
                existingProfile.setStudentName(healthProfile.getStudentName());
                System.out.println("Updated studentName: " + healthProfile.getStudentName());
            }
            
            if (healthProfile.getClassName() != null) {
                existingProfile.setClassName(healthProfile.getClassName());
                System.out.println("Updated className: " + healthProfile.getClassName());
            }
            
            if (healthProfile.getGrade() != null) {
                existingProfile.setGrade(healthProfile.getGrade());
                System.out.println("Updated grade: " + healthProfile.getGrade());
            }
            
            if (healthProfile.getGender() != null) {
                existingProfile.setGender(healthProfile.getGender());
                System.out.println("Updated gender: " + healthProfile.getGender());
            }
            
            if (healthProfile.getDateOfBirth() != null) {
                existingProfile.setDateOfBirth(healthProfile.getDateOfBirth());
                System.out.println("Updated dateOfBirth: " + healthProfile.getDateOfBirth());
            }
            
            if (healthProfile.getCity() != null) {
                existingProfile.setCity(healthProfile.getCity());
                System.out.println("Updated city: " + healthProfile.getCity());
            }
            
            if (healthProfile.getDistrict() != null) {
                existingProfile.setDistrict(healthProfile.getDistrict());
                System.out.println("Updated district: " + healthProfile.getDistrict());
            }
            
            // Cập nhật các field text (có thể null)
            existingProfile.setAllergies(healthProfile.getAllergies());
            existingProfile.setChronicDiseases(healthProfile.getChronicDiseases());
            existingProfile.setMedicalHistory(healthProfile.getMedicalHistory());
            existingProfile.setVaccinationHistory(healthProfile.getVaccinationHistory());
            existingProfile.setVisionDetails(healthProfile.getVisionDetails());
            existingProfile.setHearingDetails(healthProfile.getHearingDetails());
            
            System.out.println("Updated text fields - allergies: " + healthProfile.getAllergies());
            System.out.println("Updated text fields - chronicDiseases: " + healthProfile.getChronicDiseases());
            
            // Cập nhật student relationship nếu có
            if (healthProfile.getStudent() != null && healthProfile.getStudent().getId() != null) {
                Student student = studentRepository.findById(healthProfile.getStudent().getId())
                        .orElseThrow(() -> new RuntimeException("Student not found with ID: " + healthProfile.getStudent().getId()));
                existingProfile.setStudent(student);
                System.out.println("Updated student relationship: " + student.getId());
                
                // Đồng bộ thông tin từ HealthProfile vào Student
                syncStudentInfoFromHealthProfile(student, existingProfile);
                studentRepository.save(student);
            }
            
            // Lưu vào database
            HealthProfile saved = healthProfileRepository.save(existingProfile);
            System.out.println("Health profile updated successfully with ID: " + saved.getId());
            System.out.println("Final studentName: " + saved.getStudentName());
            System.out.println("Final className: " + saved.getClassName());
            System.out.println("Final grade: " + saved.getGrade());
            
            return saved;
        } catch (Exception e) {
            System.err.println("Error updating health profile: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteHealthProfile(Long id) {
        healthProfileRepository.deleteById(id);
    }

    public Optional<HealthProfile> findByStudentId(Long studentId) {
        return healthProfileRepository.findByStudent_Id(studentId);
    }
    
    /**
     * Đồng bộ thông tin từ HealthProfile vào Student
     */
    private void syncStudentInfoFromHealthProfile(Student student, HealthProfile healthProfile) {
        // Cập nhật thông tin cơ bản của student từ health profile
        if (healthProfile.getStudentName() != null && !healthProfile.getStudentName().trim().isEmpty()) {
            student.setFullName(healthProfile.getStudentName());
        }
        
        if (healthProfile.getDateOfBirth() != null) {
            student.setDateOfBirth(healthProfile.getDateOfBirth());
        }
        
        if (healthProfile.getGender() != null && !healthProfile.getGender().trim().isEmpty()) {
            try {
                Gender gender = Gender.valueOf(healthProfile.getGender().toUpperCase());
                student.setGender(gender);
            } catch (IllegalArgumentException e) {
                // Nếu không parse được enum, giữ nguyên giá trị hiện tại
                System.out.println("Invalid gender value: " + healthProfile.getGender());
            }
        }
        
        if (healthProfile.getClassName() != null && !healthProfile.getClassName().trim().isEmpty()) {
            student.setStudentClass(healthProfile.getClassName());
        }
        
        if (healthProfile.getCity() != null && !healthProfile.getCity().trim().isEmpty()) {
            student.setCity(healthProfile.getCity());
        }
        
        if (healthProfile.getDistrict() != null && !healthProfile.getDistrict().trim().isEmpty()) {
            student.setDistrict(healthProfile.getDistrict());
        }
        
        if (healthProfile.getGrade() != null && !healthProfile.getGrade().trim().isEmpty()) {
            student.setGrade(healthProfile.getGrade());
        }
        
        System.out.println("Student info synchronized from health profile");
    }
}
