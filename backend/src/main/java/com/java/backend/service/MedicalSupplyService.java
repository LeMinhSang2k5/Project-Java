package com.java.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.backend.entity.MedicalSupply;
import com.java.backend.repository.MedicalSupplyRepository;


@Service
public class MedicalSupplyService {
    @Autowired
    private MedicalSupplyRepository medicalSupplyRepository;

    public List<MedicalSupply> getAllMedicalSupplies() {
        return medicalSupplyRepository.findAll();
    }

    public MedicalSupply getMedicalSupplyById(Long id) {
        return medicalSupplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical supply not found with id " + id));
    }

    public MedicalSupply createMedicalSupply(MedicalSupply medicalSupply) {
        return medicalSupplyRepository.save(medicalSupply);
    }

    public MedicalSupply updateMedicalSupply(Long id, MedicalSupply medicalSupplyDetails) {
        return medicalSupplyRepository.save(medicalSupplyDetails);
}
}
