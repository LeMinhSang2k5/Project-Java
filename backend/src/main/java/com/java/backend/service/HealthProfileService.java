package com.java.backend.service;

import com.java.backend.entity.HealthProfile;
import com.java.backend.entity.Student;
import com.java.backend.repository.HealthProfileRepository;
import com.java.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HealthProfileService {

    @Autowired
    private HealthProfileRepository healthProfileRepository;

    @Autowired
    private StudentRepository studentRepository;

    public HealthProfile saveHealthProfile(HealthProfile healthProfile) {
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

    public void deleteHealthProfile(Long id) {
        healthProfileRepository.deleteById(id);
    }
    // @Transactional
    // public HealthProfile saveHealthProfile(HealthProfile healthProfile) {
    //     // Create new student
    //     Student student = new Student();
    //     student.setFullName(healthProfile.getStudentName());
    //     student.setStudentClass(healthProfile.getClassName());
    //     student.setGrade(healthProfile.getGrade());
    //     student.setGender(healthProfile.getGender());
    //     student.setDateOfBirth(healthProfile.getDateOfBirth());
    //     student.setCity(healthProfile.getCity());
    //     student.setDistrict(healthProfile.getDistrict());
        
    //     // Save student first
    //     Student savedStudent = studentRepository.save(student);
        
    //     // Set student to health profile
    //     healthProfile.setStudent(savedStudent);
        
    //     // Save health profile
    //     return healthProfileRepository.save(healthProfile);
    // }

    // // public List<HealthProfile> getAllHealthProfiles() {
    // //     return healthProfileRepository.findAll();
    // // }

    // // public HealthProfile getHealthProfileById(Long id) {
    // //     return healthProfileRepository.findById(id)
    // //             .orElseThrow(() -> new RuntimeException("Health profile not found with id: " + id));
    // // } 
    
    

} 