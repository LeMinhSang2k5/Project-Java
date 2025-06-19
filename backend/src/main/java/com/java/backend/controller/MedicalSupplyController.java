package com.java.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.backend.entity.MedicalSupply;
import com.java.backend.repository.MedicalSupplyRepository;

@RestController
@RequestMapping("/api/medical-supplies")
@CrossOrigin(origins = "http://localhost:3000")
public class MedicalSupplyController {

    @Autowired
    private MedicalSupplyRepository medicalSupplyRepository;

    @PostMapping
    public MedicalSupply createMedicalSupply(@RequestBody MedicalSupply medicalSupply) {
        return medicalSupplyRepository.save(medicalSupply);
    }

    @GetMapping
    public List<MedicalSupply> getAllMedicalSupplies() {
        return medicalSupplyRepository.findAll();
    }
}
