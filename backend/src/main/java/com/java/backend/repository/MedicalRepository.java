package com.java.backend.repository;

import com.java.backend.entity.Medical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicalRepository extends JpaRepository<Medical, Long> {
    // Tìm tất cả yêu cầu thuốc theo student ID
    List<Medical> findByStudentIdOrderByRequestDateDesc(Long studentId);
    
    // Tìm tất cả yêu cầu thuốc theo parent ID
    List<Medical> findByParentIdOrderByRequestDateDesc(Long parentId);
    
    // Tìm tất cả yêu cầu thuốc theo status
    List<Medical> findByStatusOrderByRequestDateDesc(String status);
    
    // Tìm yêu cầu thuốc theo student ID và status
    List<Medical> findByStudentIdAndStatusOrderByRequestDateDesc(Long studentId, String status);
}
