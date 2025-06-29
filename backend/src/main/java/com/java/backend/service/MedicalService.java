package com.java.backend.service;

import java.util.List;
import java.util.Optional;

import com.java.backend.entity.Medical;
import com.java.backend.repository.MedicalRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalService {
    @Autowired
    private MedicalRepository medicalRepository;

    public List<Medical> getAllMedicals() {
        return medicalRepository.findAll();
    }

    public Medical getMedicalById(Long id) {
        return medicalRepository.findById(id).orElse(null);
    }

    public Medical createMedical(Medical medical) {
        return medicalRepository.save(medical);
    }

    public Medical updateMedical(Long id, Medical medical) {
        return medicalRepository.save(medical);
    }

    public void deleteMedical(Long id) {
        medicalRepository.deleteById(id);
    }

    public Optional<Medical> getMedicalsByParentId(Long parentId) {
        return medicalRepository.findByParentId(parentId);
    }
}

