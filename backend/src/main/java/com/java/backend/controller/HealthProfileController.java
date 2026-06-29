package com.java.backend.controller;

import com.java.backend.entity.HealthProfile;
import com.java.backend.entity.Student;
import com.java.backend.exception.HealthProfileNotFoundException;
import com.java.backend.exception.ParentNotFoundException;
import com.java.backend.exception.StudentNotFoundException;
import com.java.backend.service.HealthProfileService;
import com.java.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/health-profiles")
@CrossOrigin(origins = "http://localhost:3000")
public class HealthProfileController {

    @Autowired
    private HealthProfileService healthProfileService;

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<?> createHealthProfile(@RequestBody HealthProfile healthProfile) {
        try {
            HealthProfile saved = healthProfileService.saveHealthProfile(healthProfile);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (StudentNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Lỗi khi tạo hồ sơ sức khỏe: " + e.getMessage()));
        }
    }

    @GetMapping
    public List<HealthProfile> getAllHealthProfiles() {
        return healthProfileService.getAllHealthProfiles();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getByStudentId(@PathVariable Long studentId) {
        try {
            return ResponseEntity.ok(healthProfileService.findByStudentIdOrThrow(studentId));
        } catch (HealthProfileNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/by-parent/{parentId}/student")
    public ResponseEntity<?> getStudentIdByParentId(@PathVariable Long parentId) {
        try {
            List<Student> students = studentService.getStudentsByParent(parentId);
            if (students != null && !students.isEmpty()) {
                return ResponseEntity.ok(students.get(0).getId());
            }
            return ResponseEntity.status(404)
                    .body(Map.of("message", "Phụ huynh chưa có học sinh nào được liên kết"));
        } catch (ParentNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Lỗi khi lấy học sinh theo phụ huynh: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHealthProfile(@PathVariable Long id,
            @RequestBody HealthProfile healthProfile) {
        try {
            healthProfile.setId(id);
            HealthProfile updated = healthProfileService.updateHealthProfile(healthProfile);
            return ResponseEntity.ok(updated);
        } catch (HealthProfileNotFoundException | StudentNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Lỗi khi cập nhật hồ sơ sức khỏe: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHealthProfile(@PathVariable Long id) {
        try {
            healthProfileService.deleteHealthProfile(id);
            return ResponseEntity.ok(Map.of("message", "Xóa hồ sơ sức khỏe thành công"));
        } catch (HealthProfileNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}
