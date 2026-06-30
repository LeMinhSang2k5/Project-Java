package com.java.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.backend.entity.HealthProfile;
import com.java.backend.entity.Student;
import com.java.backend.exception.HealthProfileNotFoundException;
import com.java.backend.exception.StudentNotFoundException;
import com.java.backend.repository.HealthProfileRepository;
import com.java.backend.repository.StudentRepository;
import com.java.backend.enums.Gender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class HealthProfileService {
    private static final Logger logger = LoggerFactory.getLogger(HealthProfileService.class);


    @Autowired
    private HealthProfileRepository healthProfileRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public HealthProfile saveHealthProfile(HealthProfile healthProfile) {
        if (healthProfile.getStudent() == null || healthProfile.getStudent().getId() == null) {
            throw new IllegalArgumentException("Phải cung cấp ID học sinh khi tạo hồ sơ sức khỏe");
        }

        Long studentId = healthProfile.getStudent().getId();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

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
                .orElseThrow(() -> new HealthProfileNotFoundException(id));
    }

    @Transactional
    public HealthProfile updateHealthProfile(HealthProfile healthProfile) {
        try {
            logger.info("Starting update for health profile ID: {}", healthProfile.getId());
            
            if (!healthProfileRepository.existsById(healthProfile.getId())) {
                throw new HealthProfileNotFoundException(healthProfile.getId());
            }

            HealthProfile existingProfile = healthProfileRepository.findById(healthProfile.getId())
                    .orElseThrow(() -> new HealthProfileNotFoundException(healthProfile.getId()));
            
            logger.info("Found existing profile: {}", existingProfile.getId());
            
            // Cập nhật các field với null check
            if (healthProfile.getStudentName() != null) {
                existingProfile.setStudentName(healthProfile.getStudentName());
                logger.info("Updated studentName: {}", healthProfile.getStudentName());
            }
            
            if (healthProfile.getClassName() != null) {
                existingProfile.setClassName(healthProfile.getClassName());
                logger.info("Updated className: {}", healthProfile.getClassName());
            }
            
            if (healthProfile.getGrade() != null) {
                existingProfile.setGrade(healthProfile.getGrade());
                logger.info("Updated grade: {}", healthProfile.getGrade());
            }
            
            if (healthProfile.getGender() != null) {
                existingProfile.setGender(healthProfile.getGender());
                logger.info("Updated gender: {}", healthProfile.getGender());
            }
            
            if (healthProfile.getDateOfBirth() != null) {
                existingProfile.setDateOfBirth(healthProfile.getDateOfBirth());
                logger.info("Updated dateOfBirth: {}", healthProfile.getDateOfBirth());
            }
            
            if (healthProfile.getCity() != null) {
                existingProfile.setCity(healthProfile.getCity());
                logger.info("Updated city: {}", healthProfile.getCity());
            }
            
            if (healthProfile.getDistrict() != null) {
                existingProfile.setDistrict(healthProfile.getDistrict());
                logger.info("Updated district: {}", healthProfile.getDistrict());
            }
            
            // Cập nhật các field text (có thể null)
            existingProfile.setAllergies(healthProfile.getAllergies());
            existingProfile.setChronicDiseases(healthProfile.getChronicDiseases());
            existingProfile.setMedicalHistory(healthProfile.getMedicalHistory());
            existingProfile.setVaccinationHistory(healthProfile.getVaccinationHistory());
            existingProfile.setVisionDetails(healthProfile.getVisionDetails());
            existingProfile.setHearingDetails(healthProfile.getHearingDetails());
            
            logger.info("Updated text fields - allergies: {}", healthProfile.getAllergies());
            logger.info("Updated text fields - chronicDiseases: {}", healthProfile.getChronicDiseases());
            
            // Cập nhật student relationship nếu có
            if (healthProfile.getStudent() != null && healthProfile.getStudent().getId() != null) {
                Student student = studentRepository.findById(healthProfile.getStudent().getId())
                        .orElseThrow(() -> new StudentNotFoundException(healthProfile.getStudent().getId()));
                existingProfile.setStudent(student);
                logger.info("Updated student relationship: {}", student.getId());
                
                // Đồng bộ thông tin từ HealthProfile vào Student
                syncStudentInfoFromHealthProfile(student, existingProfile);
                studentRepository.save(student);
            }
            
            // Lưu vào database
            HealthProfile saved = healthProfileRepository.save(existingProfile);
            logger.info("Health profile updated successfully with ID: {}", saved.getId());
            logger.info("Final studentName: {}", saved.getStudentName());
            logger.info("Final className: {}", saved.getClassName());
            logger.info("Final grade: {}", saved.getGrade());
            
            return saved;
        } catch (Exception e) {
            logger.error("Error updating health profile: {}", e.getMessage());
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public void deleteHealthProfile(Long id) {
        if (!healthProfileRepository.existsById(id)) {
            throw new HealthProfileNotFoundException(id);
        }
        healthProfileRepository.deleteById(id);
    }

    public HealthProfile findByStudentIdOrThrow(Long studentId) {
        return healthProfileRepository.findByStudent_Id(studentId)
                .orElseThrow(() -> new HealthProfileNotFoundException(studentId, true));
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
                logger.info("Invalid gender value: {}", healthProfile.getGender());
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
        
        logger.info("Student info synchronized from health profile");
    }
}
