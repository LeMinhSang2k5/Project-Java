package com.java.backend.controller;

import java.util.List;

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
    public ResponseEntity<SchoolHealthDoc> getSchoolHealthDocById(@PathVariable Long id) {
        return ResponseEntity.ok(schoolHealthDocService.getSchoolHealthDocById(id));
    }

    @PostMapping
    public ResponseEntity<SchoolHealthDoc> createSchoolHealthDoc(@RequestBody SchoolHealthDoc schoolHealthDoc) {
        return ResponseEntity.ok(schoolHealthDocService.createSchoolHealthDoc(schoolHealthDoc));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchoolHealthDoc> updateSchoolHealthDoc(@PathVariable Long id, @RequestBody SchoolHealthDoc schoolHealthDoc) {
        return ResponseEntity.ok(schoolHealthDocService.updateSchoolHealthDoc(id, schoolHealthDoc));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchoolHealthDoc(@PathVariable Long id) {
        schoolHealthDocService.deleteSchoolHealthDoc(id);
        return ResponseEntity.ok().build();
    }
}
