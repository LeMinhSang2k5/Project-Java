package com.java.backend.service;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.stereotype.Service;

import com.java.backend.entity.HealthIncident;
import com.java.backend.repository.HealthIncidentRepository;

@Service
public class HealthIncidentService  {

    private final HealthIncidentRepository healthIncidentRepository;

    public HealthIncidentService(HealthIncidentRepository healthIncidentRepository) {
        this.healthIncidentRepository = healthIncidentRepository;
    }

    public HealthIncident createHealthIncident(HealthIncident healthIncident) {
        return healthIncidentRepository.save(healthIncident);
    }

    public HealthIncident getHealthIncidentById(Long id) {
        return healthIncidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Health incident not found with id: " + id));
    }

    public List<HealthIncident> getAllHealthIncidents() {
        return healthIncidentRepository.findAll();
    }

    public HealthIncident updateHealthIncident(Long id, HealthIncident healthIncident) {
        HealthIncident existingHealthIncident = getHealthIncidentById(id);
        existingHealthIncident.setIncidentType(healthIncident.getIncidentType());
        existingHealthIncident.setDescription(healthIncident.getDescription());
        existingHealthIncident.setActionTaken(healthIncident.getActionTaken());
        return healthIncidentRepository.save(existingHealthIncident);
    }

    public void deleteHealthIncident(Long id) {
        healthIncidentRepository.deleteById(id);
    }

    public List<HealthIncident> getHealthIncidentsByStudentId(Long studentId) {
        return healthIncidentRepository.findByStudent_Id(studentId);
    }

    public List<HealthIncident> getHealthIncidentsByReportedById(Long reportedById) {
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

    public List<HealthIncident> getHealthIncidentsByIncidentTimeAndStatus(LocalDateTime incidentTime, HealthIncident.IncidentStatus status) {
        return healthIncidentRepository.findByIncidentTimeAndStatus(incidentTime, status);
    }

    public List<HealthIncident> getHealthIncidentsByStudentIdAndIncidentTime(Long studentId, LocalDateTime incidentTime) {
        return healthIncidentRepository.findByStudent_IdAndIncidentTime(studentId, incidentTime);
    }
}
