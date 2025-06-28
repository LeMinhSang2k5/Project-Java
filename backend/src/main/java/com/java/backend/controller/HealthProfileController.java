package com.java.backend.controller;

import com.java.backend.entity.HealthProfile;
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

    // Tạo health profile trực tiếp
    @PostMapping
    public ResponseEntity<HealthProfile> createHealthProfile(@RequestBody HealthProfile healthProfile) {
        HealthProfile saved = healthProfileService.saveHealthProfile(healthProfile);
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHealthProfile(@PathVariable Long id,
            @RequestBody HealthProfile healthProfile) {
        try {
            // Đảm bảo ID khớp
            healthProfile.setId(id);
            
            // Validate student ID if provided
            if (healthProfile.getStudent() != null && healthProfile.getStudent().getId() != null) {
                System.out.println("Updating health profile for student ID: " + healthProfile.getStudent().getId());
            }
            
            // Gọi service để cập nhật - service sẽ xử lý logic cập nhật đúng cách
            HealthProfile updated = healthProfileService.updateHealthProfile(healthProfile);
            System.out.println("Controller: Health profile updated successfully with ID: " + updated.getId());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi cập nhật hồ sơ sức khỏe: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteHealthProfile(@PathVariable Long id) {
        healthProfileService.deleteHealthProfile(id);
    }
}