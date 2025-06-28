package com.java.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.backend.entity.HealthProfile;
import com.java.backend.entity.Parent;
import com.java.backend.entity.Student;
import com.java.backend.enums.Role;
import com.java.backend.repository.ParentRepository;
import com.java.backend.repository.StudentRepository;
import com.java.backend.repository.HealthProfileRepository;

@Service
public class ParentService {
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private HealthProfileRepository healthProfileRepository;

    public Parent saveParent(Parent parent) {
        parent.setRole(Role.PARENT);
        return parentRepository.save(parent);
    }

    public List<Parent> getAllParents() {
        return parentRepository.findAll();
    }

    public Parent getParentById(Long id) {
        return parentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent not found with id: " + id));
    }

    public Parent getParentByEmail(String email) {
        return parentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Parent not found with email: " + email));
    }

    public Parent updateParent(Parent parent) {
        return parentRepository.save(parent);
    }

    public void deleteParent(Long id) {
        parentRepository.deleteById(id);
    }

    public List<Student> getStudentsOfParent(Long parentId) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found with id: " + parentId));
        return parent.getStudents();
    }

    public void linkStudentToParent(Long parentId, Long studentId) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        student.setParent(parent);
        studentRepository.save(student);
    }

    public HealthProfile findByStudentId(Long studentId) {
        return healthProfileRepository.findByStudent_Id(studentId)
                .orElseThrow(() -> new RuntimeException("Health profile not found for student ID: " + studentId));
    }
}