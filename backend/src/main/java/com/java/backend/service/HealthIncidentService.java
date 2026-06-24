package com.java.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.java.backend.entity.HealthIncident;
import com.java.backend.entity.Student;
import com.java.backend.entity.User;
import com.java.backend.repository.HealthIncidentRepository;
import com.java.backend.repository.StudentRepository;
import com.java.backend.repository.UserRepository;

@Service
public class HealthIncidentService {

    private final HealthIncidentRepository healthIncidentRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public HealthIncidentService(
            HealthIncidentRepository healthIncidentRepository,
            StudentRepository studentRepository,
            UserRepository userRepository) {
        this.healthIncidentRepository = healthIncidentRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public HealthIncident createHealthIncident(HealthIncident healthIncident) {
        Long studentId = healthIncident.getStudent().getId();
        Long userId = healthIncident.getReportedBy().getId();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException(
                        "Student not found with id: " + studentId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(
                        "User not found with id: " + userId));

        healthIncident.setStudent(student);
        healthIncident.setReportedBy(user);

        return healthIncidentRepository.save(healthIncident);
    }

    // GET BY ID
    public HealthIncident getHealthIncidentById(Long id) {
        return healthIncidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Health incident not found with id: " + id));
    }

    // GET ALL
    public List<HealthIncident> getAllHealthIncidents() {
        return healthIncidentRepository.findAll();
    }

    // UPDATE
    public HealthIncident updateHealthIncident(Long id, HealthIncident healthIncident) {
        HealthIncident existingHealthIncident = getHealthIncidentById(id);

        existingHealthIncident.setIncidentType(
                healthIncident.getIncidentType());

        existingHealthIncident.setDescription(
                healthIncident.getDescription());

        existingHealthIncident.setActionTaken(
                healthIncident.getActionTaken());

        existingHealthIncident.setIncidentTime(
                healthIncident.getIncidentTime());

        existingHealthIncident.setStatus(
                healthIncident.getStatus());

        // update student nếu có
        if (healthIncident.getStudent() != null) {
            Long studentId = healthIncident.getStudent().getId();

            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException(
                            "Student not found with id: " + studentId));

            existingHealthIncident.setStudent(student);
        }

        // update reportedBy nếu có
        if (healthIncident.getReportedBy() != null) {
            Long userId = healthIncident.getReportedBy().getId();

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException(
                            "User not found with id: " + userId));

            existingHealthIncident.setReportedBy(user);
        }

        return healthIncidentRepository.save(existingHealthIncident);
    }

    // DELETE
    public void deleteHealthIncident(Long id) {
        HealthIncident existingHealthIncident = getHealthIncidentById(id);
        healthIncidentRepository.delete(existingHealthIncident);
    }

    // FILTER BY STUDENT
    public List<HealthIncident> getHealthIncidentsByStudentId(Long studentId) {
        return healthIncidentRepository.findByStudent_Id(studentId);
    }

    // FILTER BY REPORTED BY
    public List<HealthIncident> getHealthIncidentsByReportedById(Long reportedById) {
        return healthIncidentRepository.findByReportedBy_Id(reportedById);
    }

    // FILTER BY TIME
    public List<HealthIncident> getHealthIncidentsByIncidentTime(LocalDateTime incidentTime) {
        return healthIncidentRepository.findByIncidentTime(incidentTime);
    }

    // FILTER BY STATUS
    public List<HealthIncident> getHealthIncidentsByStatus(
            HealthIncident.IncidentStatus status) {
        return healthIncidentRepository.findByStatus(status);
    }

    // FILTER BY TYPE
    public List<HealthIncident> getHealthIncidentsByIncidentType(
            HealthIncident.IncidentType incidentType) {
        return healthIncidentRepository.findByIncidentType(incidentType);
    }

    // FILTER BY TIME + STATUS
    public List<HealthIncident> getHealthIncidentsByIncidentTimeAndStatus(
            LocalDateTime incidentTime,
            HealthIncident.IncidentStatus status) {
        return healthIncidentRepository.findByIncidentTimeAndStatus(
                incidentTime, status);
    }

    // FILTER BY STUDENT + TIME
    public List<HealthIncident> getHealthIncidentsByStudentIdAndIncidentTime(
            Long studentId,
            LocalDateTime incidentTime) {
        return healthIncidentRepository.findByStudent_IdAndIncidentTime(
                studentId, incidentTime);
    }
}