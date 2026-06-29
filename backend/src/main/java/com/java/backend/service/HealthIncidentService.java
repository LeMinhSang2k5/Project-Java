package com.java.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.java.backend.entity.HealthIncident;
import com.java.backend.entity.Student;
import com.java.backend.entity.User;
import com.java.backend.exception.HealthIncidentNotFoundException;
import com.java.backend.exception.StudentNotFoundException;
import com.java.backend.exception.UserNotFoundException;
import com.java.backend.repository.HealthIncidentRepository;
import com.java.backend.repository.IncidentSupplyUsageRepository;
import com.java.backend.repository.StudentRepository;
import com.java.backend.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class HealthIncidentService {

    private final HealthIncidentRepository healthIncidentRepository;
    private final IncidentSupplyUsageRepository incidentSupplyUsageRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public HealthIncidentService(
            HealthIncidentRepository healthIncidentRepository,
            IncidentSupplyUsageRepository incidentSupplyUsageRepository,
            StudentRepository studentRepository,
            UserRepository userRepository) {
        this.healthIncidentRepository = healthIncidentRepository;
        this.incidentSupplyUsageRepository = incidentSupplyUsageRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    public HealthIncident createHealthIncident(HealthIncident healthIncident) {
        if (healthIncident.getStudent() == null || healthIncident.getStudent().getId() == null) {
            throw new IllegalArgumentException("Phải cung cấp ID học sinh khi tạo sự cố y tế");
        }
        if (healthIncident.getReportedBy() == null || healthIncident.getReportedBy().getId() == null) {
            throw new IllegalArgumentException("Phải cung cấp ID người báo cáo khi tạo sự cố y tế");
        }
        if (healthIncident.getDescription() == null || healthIncident.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Mô tả sự cố không được để trống");
        }

        Long studentId = healthIncident.getStudent().getId();
        Long userId = healthIncident.getReportedBy().getId();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        healthIncident.setStudent(student);
        healthIncident.setReportedBy(user);

        return healthIncidentRepository.save(healthIncident);
    }

    public HealthIncident getHealthIncidentById(Long id) {
        return healthIncidentRepository.findById(id)
                .orElseThrow(() -> new HealthIncidentNotFoundException(id));
    }

    public List<HealthIncident> getAllHealthIncidents() {
        return healthIncidentRepository.findAll();
    }

    public HealthIncident updateHealthIncident(Long id, HealthIncident healthIncident) {
        HealthIncident existingHealthIncident = getHealthIncidentById(id);

        existingHealthIncident.setIncidentType(healthIncident.getIncidentType());
        existingHealthIncident.setDescription(healthIncident.getDescription());
        existingHealthIncident.setActionTaken(healthIncident.getActionTaken());
        existingHealthIncident.setIncidentTime(healthIncident.getIncidentTime());
        existingHealthIncident.setStatus(healthIncident.getStatus());

        if (healthIncident.getStudent() != null && healthIncident.getStudent().getId() != null) {
            Long studentId = healthIncident.getStudent().getId();
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new StudentNotFoundException(studentId));
            existingHealthIncident.setStudent(student);
        }

        if (healthIncident.getReportedBy() != null && healthIncident.getReportedBy().getId() != null) {
            Long userId = healthIncident.getReportedBy().getId();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));
            existingHealthIncident.setReportedBy(user);
        }

        return healthIncidentRepository.save(existingHealthIncident);
    }

    @Transactional
    public void deleteHealthIncident(Long id) {
        if (!healthIncidentRepository.existsById(id)) {
            throw new HealthIncidentNotFoundException(id);
        }
        incidentSupplyUsageRepository.deleteByIncidentId(id);
        healthIncidentRepository.deleteById(id);
    }

    public List<HealthIncident> getHealthIncidentsByStudentId(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }
        return healthIncidentRepository.findByStudent_Id(studentId);
    }

    public List<HealthIncident> getHealthIncidentsByReportedById(Long reportedById) {
        if (!userRepository.existsById(reportedById)) {
            throw new UserNotFoundException(reportedById);
        }
        return healthIncidentRepository.findByReportedBy_Id(reportedById);
    }

    public List<HealthIncident> getHealthIncidentsByIncidentTime(LocalDateTime incidentTime) {
        return healthIncidentRepository.findByIncidentTime(incidentTime);
    }

    public List<HealthIncident> getHealthIncidentsByStatus(HealthIncident.IncidentStatus status) {
        return healthIncidentRepository.findByStatus(status);
    }

    public List<HealthIncident> getHealthIncidentsByIncidentType(HealthIncident.IncidentType incidentType) {
        return healthIncidentRepository.findByIncidentType(incidentType);
    }

    public List<HealthIncident> getHealthIncidentsByIncidentTimeAndStatus(
            LocalDateTime incidentTime,
            HealthIncident.IncidentStatus status) {
        return healthIncidentRepository.findByIncidentTimeAndStatus(incidentTime, status);
    }

    public List<HealthIncident> getHealthIncidentsByStudentIdAndIncidentTime(
            Long studentId,
            LocalDateTime incidentTime) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }
        return healthIncidentRepository.findByStudent_IdAndIncidentTime(studentId, incidentTime);
    }
}
