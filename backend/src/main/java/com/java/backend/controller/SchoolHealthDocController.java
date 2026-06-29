package com.java.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.java.backend.entity.SchoolHealthDoc;
import com.java.backend.exception.SchoolHealthDocNotFoundException;
import com.java.backend.service.SchoolHealthDocService;

@RestController
@RequestMapping("/api/school-health-docs")
@CrossOrigin(origins = "http://localhost:3000")
public class SchoolHealthDocController {

    @Autowired
    private SchoolHealthDocService schoolHealthDocService;

    @GetMapping
    public ResponseEntity<List<SchoolHealthDoc>> getAllSchoolHealthDocs() {
        return ResponseEntity.ok(schoolHealthDocService.getAllSchoolHealthDocs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSchoolHealthDocById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(schoolHealthDocService.getSchoolHealthDocById(id));
        } catch (SchoolHealthDocNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createSchoolHealthDoc(@RequestBody SchoolHealthDoc schoolHealthDoc) {
        ResponseEntity<?> validationError = validateSchoolHealthDoc(schoolHealthDoc);
        if (validationError != null) {
            return validationError;
        }
        return ResponseEntity.ok(schoolHealthDocService.createSchoolHealthDoc(schoolHealthDoc));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchoolHealthDoc(@PathVariable Long id, @RequestBody SchoolHealthDoc schoolHealthDoc) {
        ResponseEntity<?> validationError = validateSchoolHealthDoc(schoolHealthDoc);
        if (validationError != null) {
            return validationError;
        }
        try {
            return ResponseEntity.ok(schoolHealthDocService.updateSchoolHealthDoc(id, schoolHealthDoc));
        } catch (SchoolHealthDocNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchoolHealthDoc(@PathVariable Long id) {
        try {
            schoolHealthDocService.deleteSchoolHealthDoc(id);
            return ResponseEntity.ok(Map.of("message", "Xóa tài liệu thành công"));
        } catch (SchoolHealthDocNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    private ResponseEntity<?> validateSchoolHealthDoc(SchoolHealthDoc schoolHealthDoc) {
        if (schoolHealthDoc.getTitle() == null || schoolHealthDoc.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tiêu đề không được để trống"));
        }
        if (schoolHealthDoc.getContent() == null || schoolHealthDoc.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Nội dung không được để trống"));
        }
        if (schoolHealthDoc.getUrl() == null || schoolHealthDoc.getUrl().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "URL không được để trống"));
        }
        return null;
    }
}
