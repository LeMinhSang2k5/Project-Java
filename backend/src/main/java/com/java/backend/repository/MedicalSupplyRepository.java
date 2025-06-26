package com.java.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.backend.entity.MedicalSupply;

@Repository
public interface MedicalSupplyRepository extends JpaRepository<MedicalSupply, Long> {
    
    // Tìm vật tư y tế theo tên chính xác
    Optional<MedicalSupply> findByName(String name);
    
    // Tìm vật tư y tế theo tên (tìm kiếm mờ, không phân biệt hoa thường)
    List<MedicalSupply> findByNameContainingIgnoreCase(String name);
    
    // Tìm vật tư y tế theo danh mục
    List<MedicalSupply> findByCategory(String category);
    
    // Tìm vật tư y tế có số lượng thấp (dưới ngưỡng)
    List<MedicalSupply> findByQuantityLessThan(Integer threshold);
    
    // Tìm vật tư y tế theo danh mục và số lượng
    List<MedicalSupply> findByCategoryAndQuantityLessThan(String category, Integer threshold);
}
