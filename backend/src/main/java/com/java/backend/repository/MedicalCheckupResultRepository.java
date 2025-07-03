package com.java.backend.repository;

import com.java.backend.entity.MedicalCheckupResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicalCheckupResultRepository extends JpaRepository<MedicalCheckupResult, Long> {
    List<MedicalCheckupResult> findByStudentId(Long studentId);
}