package com.java.backend.controller;

import com.java.backend.entity.Parent;
import com.java.backend.entity.Student;
import com.java.backend.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/parent")
@CrossOrigin(origins = "http://localhost:3000")
public class ParentController {

    @Autowired
    private ParentService parentService;

    @PostMapping
    public ResponseEntity<Parent> createParent(@RequestBody Parent parent) {
        Parent saved = parentService.saveParent(parent);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{parentId}/students")
    public ResponseEntity<?> getStudentsOfParent(@PathVariable Long parentId) {
        try {
            List<Student> students = parentService.getStudentsOfParent(parentId);
            if (students != null && !students.isEmpty()) {
                return ResponseEntity.ok(students);
            } else {
                return ResponseEntity.ok("Phụ huynh chưa có học sinh nào được liên kết.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parent> getParent(@PathVariable Long id) {
        try {
            Parent parent = parentService.getParentById(id);
            return ResponseEntity.ok(parent);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/link-student")
    public ResponseEntity<?> linkStudentToParent(@RequestParam Long parentId, @RequestParam Long studentId) {
        try {
            parentService.linkStudentToParent(parentId, studentId);
            return ResponseEntity.ok("Liên kết học sinh với phụ huynh thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi liên kết: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParent(@PathVariable Long id) {
        try {
            parentService.deleteParent(id);
            return ResponseEntity.ok("Xóa thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi xóa phụ huynh: " + e.getMessage());
        }
    }

    // Endpoint để xác nhận phiếu tiêm chủng/kiểm tra y tế
    @PostMapping("/confirm/{formId}")
    public ResponseEntity<Map<String, String>> confirmForm(@PathVariable Long formId) {
        try {
            // Mock logic - trong thực tế sẽ cập nhật database
            Map<String, String> response = new HashMap<>();
            response.put("message", "Xác nhận thành công cho phiếu ID: " + formId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Xác nhận thất bại");
            return ResponseEntity.badRequest().body(error);
        }
    }
}
