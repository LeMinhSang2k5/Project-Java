package com.java.backend.service;

import java.util.List;
import java.util.Optional;

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

    public Optional<MedicalSupply> getMedicalSupplyById(Long id) {
        return medicalSupplyRepository.findById(id);
    }

    public MedicalSupply createMedicalSupply(MedicalSupply medicalSupply) {
        // Validation
        validateMedicalSupply(medicalSupply);
        
        // Kiểm tra tên đã tồn tại
        if (medicalSupplyRepository.findByName(medicalSupply.getName()).isPresent()) {
            throw new RuntimeException("Vật tư y tế với tên này đã tồn tại");
        }
        
        return medicalSupplyRepository.save(medicalSupply);
    }

    public MedicalSupply updateMedicalSupply(Long id, MedicalSupply medicalSupplyDetails) {
        Optional<MedicalSupply> existingSupply = medicalSupplyRepository.findById(id);
        if (!existingSupply.isPresent()) {
            throw new RuntimeException("Vật tư y tế không tồn tại với ID: " + id);
        }

        MedicalSupply supplyToUpdate = existingSupply.get();
        
        // Cập nhật các trường nếu có
        if (medicalSupplyDetails.getName() != null && !medicalSupplyDetails.getName().trim().isEmpty()) {
            // Kiểm tra tên mới có trùng không
            Optional<MedicalSupply> duplicateName = medicalSupplyRepository.findByName(medicalSupplyDetails.getName());
            if (duplicateName.isPresent() && !duplicateName.get().getId().equals(id)) {
                throw new RuntimeException("Vật tư y tế với tên này đã tồn tại");
            }
            supplyToUpdate.setName(medicalSupplyDetails.getName().trim());
        }
        
        if (medicalSupplyDetails.getQuantity() != null && medicalSupplyDetails.getQuantity() > 0) {
            supplyToUpdate.setQuantity(medicalSupplyDetails.getQuantity());
        }
        
        if (medicalSupplyDetails.getCategory() != null && !medicalSupplyDetails.getCategory().trim().isEmpty()) {
            supplyToUpdate.setCategory(medicalSupplyDetails.getCategory().trim());
        }
        
        if (medicalSupplyDetails.getExpiryDate() != null) {
            supplyToUpdate.setExpiryDate(medicalSupplyDetails.getExpiryDate());
        }

        return medicalSupplyRepository.save(supplyToUpdate);
    }

    public void deleteMedicalSupply(Long id) {
        if (!medicalSupplyRepository.existsById(id)) {
            throw new RuntimeException("Vật tư y tế không tồn tại với ID: " + id);
        }
        medicalSupplyRepository.deleteById(id);
    }

    public List<MedicalSupply> searchMedicalSupplies(String name) {
        return medicalSupplyRepository.findByNameContainingIgnoreCase(name);
    }

    public List<MedicalSupply> getMedicalSuppliesByCategory(String category) {
        return medicalSupplyRepository.findByCategory(category);
    }

    public List<MedicalSupply> getLowStockSupplies(Integer threshold) {
        return medicalSupplyRepository.findByQuantityLessThan(threshold);
    }

    public List<MedicalSupply> getLowStockSuppliesByCategory(String category, Integer threshold) {
        return medicalSupplyRepository.findByCategoryAndQuantityLessThan(category, threshold);
    }

    private void validateMedicalSupply(MedicalSupply medicalSupply) {
        if (medicalSupply.getName() == null || medicalSupply.getName().trim().isEmpty()) {
            throw new RuntimeException("Tên vật tư y tế không được để trống");
        }
        
        if (medicalSupply.getQuantity() == null || medicalSupply.getQuantity() <= 0) {
            throw new RuntimeException("Số lượng phải lớn hơn 0");
        }
        
        if (medicalSupply.getCategory() == null || medicalSupply.getCategory().trim().isEmpty()) {
            throw new RuntimeException("Danh mục không được để trống");
        }
    }
}
