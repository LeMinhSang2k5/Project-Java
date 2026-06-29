package com.java.backend.controller;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        return ResponseEntity.ok(healthIncidentService.getHealthIncidentById(id));
    }

    @GetMapping
    public ResponseEntity<List<HealthIncident>> getAllHealthIncidents() {
        return ResponseEntity.ok(healthIncidentService.getAllHealthIncidents());
    }

    @PutMapping("/{id}")
    public ResponseEntity<HealthIncident> updateHealthIncident(@PathVariable Long id,
            @RequestBody HealthIncident healthIncident) {
        return ResponseEntity.ok(healthIncidentService.updateHealthIncident(id, healthIncident));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteHealthIncident(@PathVariable Long id) {
        healthIncidentService.deleteHealthIncident(id);
        return ResponseEntity.ok(Map.of("message", "Xóa sự cố y tế thành công"));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<HealthIncident>> getHealthIncidentsByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(healthIncidentService.getHealthIncidentsByStudentId(studentId));
    }

    @GetMapping("/reported-by/{reportedById}")
    public ResponseEntity<List<HealthIncident>> getHealthIncidentsByReportedById(@PathVariable Long reportedById) {
        return ResponseEntity.ok(healthIncidentService.getHealthIncidentsByReportedById(reportedById));
    }

    @GetMapping("/incident-time/{incidentTime}")
    public ResponseEntity<List<HealthIncident>> getHealthIncidentsByIncidentTime(
            @PathVariable LocalDateTime incidentTime) {
        return ResponseEntity.ok(healthIncidentService.getHealthIncidentsByIncidentTime(incidentTime));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<HealthIncident>> getHealthIncidentsByStatus(@PathVariable IncidentStatus status) {
        return ResponseEntity.ok(healthIncidentService.getHealthIncidentsByStatus(status));
    }

    @GetMapping("/incident-type/{incidentType}")
    public ResponseEntity<List<HealthIncident>> getHealthIncidentsByIncidentType(
            @PathVariable IncidentType incidentType) {
        return ResponseEntity.ok(healthIncidentService.getHealthIncidentsByIncidentType(incidentType));
    }

    @GetMapping("/incident-time-and-status/{incidentTime}/{status}")
    public ResponseEntity<List<HealthIncident>> getHealthIncidentsByIncidentTimeAndStatus(
            @PathVariable LocalDateTime incidentTime,
            @PathVariable IncidentStatus status) {
        return ResponseEntity.ok(
                healthIncidentService.getHealthIncidentsByIncidentTimeAndStatus(incidentTime, status));
    }

    @GetMapping("/student-and-incident-time/{studentId}/{incidentTime}")
    public ResponseEntity<List<HealthIncident>> getHealthIncidentsByStudentIdAndIncidentTime(
            @PathVariable Long studentId,
            @PathVariable LocalDateTime incidentTime) {
        return ResponseEntity.ok(
                healthIncidentService.getHealthIncidentsByStudentIdAndIncidentTime(studentId, incidentTime));
    }
}
