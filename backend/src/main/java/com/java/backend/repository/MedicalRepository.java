package com.java.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.backend.entity.Medical;


public interface MedicalRepository extends JpaRepository<Medical, Long> {
    Optional<Medical> findByParentId(Long parentId);
}
