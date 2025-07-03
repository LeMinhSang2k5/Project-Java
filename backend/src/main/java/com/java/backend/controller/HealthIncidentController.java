package com.java.backend.controller;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.backend.entity.HealthIncident.IncidentStatus;
import com.java.backend.entity.HealthIncident.IncidentType;
import com.java.backend.entity.HealthIncident;
import com.java.backend.service.HealthIncidentService;

@RestController
@RequestMapping("/api/health-incidents")
@CrossOrigin(origins = "http://localhost:3000")
public class HealthIncidentController {

    private final HealthIncidentService healthIncidentService;

    public HealthIncidentController(HealthIncidentService healthIncidentService) {
        this.healthIncidentService = healthIncidentService;
    }

    @PostMapping
    public ResponseEntity<HealthIncident> createHealthIncident(@RequestBody HealthIncident healthIncident) {
        HealthIncident created = healthIncidentService.createHealthIncident(healthIncident);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HealthIncident> getHealthIncidentById(@PathVariable Long id) {
        HealthIncident incident = healthIncidentService.getHealthIncidentById(id);
        return ResponseEntity.ok(incident);
    }

    @GetMapping
    public ResponseEntity<List<HealthIncident>> getAllHealthIncidents() {
        List<HealthIncident> incidents = healthIncidentService.getAllHealthIncidents();
        return ResponseEntity.ok(incidents);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HealthIncident> updateHealthIncident(@PathVariable Long id, @RequestBody HealthIncident healthIncident) {
        HealthIncident updated = healthIncidentService.updateHealthIncident(id, healthIncident);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHealthIncident(@PathVariable Long id) {
        healthIncidentService.deleteHealthIncident(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<HealthIncident>> getHealthIncidentsByStudentId(@PathVariable Long studentId) {
        List<HealthIncident> incidents = healthIncidentService.getHealthIncidentsByStudentId(studentId);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/reported-by/{reportedById}")
    public ResponseEntity<List<HealthIncident>> getHealthIncidentsByReportedById(@PathVariable Long reportedById) {
        List<HealthIncident> incidents = healthIncidentService.getHealthIncidentsByReportedById(reportedById);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/incident-time/{incidentTime}")
    public ResponseEntity<List<HealthIncident>> getHealthIncidentsByIncidentTime(@PathVariable LocalDateTime incidentTime) {
        List<HealthIncident> incidents = healthIncidentService.getHealthIncidentsByIncidentTime(incidentTime);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<HealthIncident>> getHealthIncidentsByStatus(@PathVariable IncidentStatus status) {
        List<HealthIncident> incidents = healthIncidentService.getHealthIncidentsByStatus(status);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/incident-type/{incidentType}")
    public ResponseEntity<List<HealthIncident>> getHealthIncidentsByIncidentType(@PathVariable IncidentType incidentType) {
        List<HealthIncident> incidents = healthIncidentService.getHealthIncidentsByIncidentType(incidentType);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/incident-time-and-status/{incidentTime}/{status}")
    public ResponseEntity<List<HealthIncident>> getHealthIncidentsByIncidentTimeAndStatus(
            @PathVariable LocalDateTime incidentTime,
            @PathVariable IncidentStatus status) {
        List<HealthIncident> incidents = healthIncidentService.getHealthIncidentsByIncidentTimeAndStatus(incidentTime, status);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/student-and-incident-time/{studentId}/{incidentTime}")
    public ResponseEntity<List<HealthIncident>> getHealthIncidentsByStudentIdAndIncidentTime(
            @PathVariable Long studentId,
            @PathVariable LocalDateTime incidentTime) {
        List<HealthIncident> incidents = healthIncidentService.getHealthIncidentsByStudentIdAndIncidentTime(studentId, incidentTime);
        return ResponseEntity.ok(incidents);
    }
}
