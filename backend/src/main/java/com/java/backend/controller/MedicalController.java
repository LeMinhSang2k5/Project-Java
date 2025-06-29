package com.java.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.backend.entity.Medical;
import com.java.backend.service.MedicalService;

@RestController
@RequestMapping("/api/medical")
@CrossOrigin(origins = "http://localhost:3000")
public class MedicalController {
    @Autowired
    private MedicalService medicalService;

    @GetMapping
    public List<Medical> getAllMedicals() {
        return medicalService.getAllMedicals();
    }

    @GetMapping("/{id}")
    public Medical getMedicalById(@PathVariable Long id) {
        return medicalService.getMedicalById(id);
    }

    @PostMapping
    public Medical createMedical(@RequestBody Medical medical) {
        return medicalService.createMedical(medical);
    }

    @PutMapping("/{id}")
    public Medical updateMedical(@PathVariable Long id, @RequestBody Medical medical) {
        return medicalService.updateMedical(id, medical);
    }

    @DeleteMapping("/{id}")
    public void deleteMedical(@PathVariable Long id) {
        medicalService.deleteMedical(id);
    }
    @GetMapping("/parent/{parentId}")
    public Optional<Medical> getMedicalsByParentId(@PathVariable Long parentId) {
        return medicalService.getMedicalsByParentId(parentId);
    }
}
