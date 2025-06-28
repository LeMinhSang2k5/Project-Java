package com.java.backend.controller;

import com.java.backend.entity.HealthProfile;
import com.java.backend.entity.Parent;
import com.java.backend.entity.Student;
import com.java.backend.service.HealthProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/health-profiles")
@CrossOrigin(origins = "http://localhost:3000")
public class HealthProfileController {

    @Autowired
    private HealthProfileService healthProfileService;

    @Autowired
    private com.java.backend.service.ParentService parentService;

    // Tạo health profile trực tiếp
    @PostMapping
    public ResponseEntity<HealthProfile> createHealthProfile(@RequestBody HealthProfile healthProfile) {
        HealthProfile saved = healthProfileService.saveHealthProfile(healthProfile);
        return ResponseEntity.ok(saved);
    }

    // Tạo health profile qua parent
    @PostMapping("/by-parent/{parentId}")
    public ResponseEntity<?> createHealthProfileByParent(
            @PathVariable Long parentId,
            @RequestBody HealthProfile healthProfileData) {
        Parent parent = parentService.getParentById(parentId);
        if (parent == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy phụ huynh.");
        }
        Student student = parent.getStudent();
        if (student == null) {
            return ResponseEntity.badRequest().body("Phụ huynh chưa gán học sinh.");
        }
        healthProfileData.setStudent(student);
        student.setHealthProfile(healthProfileData);
        HealthProfile saved = healthProfileService.saveHealthProfile(healthProfileData);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<HealthProfile> getAllHealthProfiles() {
        return healthProfileService.getAllHealthProfiles();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<HealthProfile> getByStudentId(@PathVariable Long studentId) {
        Optional<HealthProfile> profile = healthProfileService.findByStudentId(studentId);
        return profile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-parent/{parentId}/student")
    public ResponseEntity<Long> getStudentIdByParent(@PathVariable Long parentId) {
        Parent parent = parentService.getParentById(parentId);
        if (parent == null || parent.getStudent() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(parent.getStudent().getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<HealthProfile> updateHealthProfile(@PathVariable Long id,
            @RequestBody HealthProfile healthProfile) {
        if (!id.equals(healthProfile.getId())) {
            return ResponseEntity.badRequest().build();
        }
        HealthProfile updated = healthProfileService.updateHealthProfile(healthProfile);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteHealthProfile(@PathVariable Long id) {
        healthProfileService.deleteHealthProfile(id);
    }
}