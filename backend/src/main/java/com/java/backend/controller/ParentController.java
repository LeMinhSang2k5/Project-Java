package com.java.backend.controller;

import com.java.backend.entity.Parent;
import com.java.backend.entity.Student;
import com.java.backend.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{parentId}/student")
    public ResponseEntity<?> getStudentOfParent(@PathVariable Long parentId) {
        try {
            Student student = parentService.getStudentOfParent(parentId);
            if (student != null) {
                return ResponseEntity.ok(student);
            } else {
                return ResponseEntity.ok("Phụ huynh chưa gán học sinh nào.");
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
        parentService.linkStudentToParent(parentId, studentId);
        return ResponseEntity.ok("Gán học sinh cho phụ huynh thành công");
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
}
