package com.java.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.backend.entity.HealthIncident;

public interface HealthIncidentRepository extends JpaRepository<HealthIncident, Long> {
    List<HealthIncident> findByStudent_Id(Long studentId);
    List<HealthIncident> findByReportedBy_Id(Long reportedById);
    List<HealthIncident> findByIncidentTime(LocalDateTime incidentTime);
    List<HealthIncident> findByStatus(HealthIncident.IncidentStatus status);
    List<HealthIncident> findByIncidentType(HealthIncident.IncidentType incidentType);
    List<HealthIncident> findByIncidentTimeAndStatus(LocalDateTime incidentTime, HealthIncident.IncidentStatus status);
    List<HealthIncident> findByStudent_IdAndIncidentTime(Long studentId, LocalDateTime incidentTime);
}
