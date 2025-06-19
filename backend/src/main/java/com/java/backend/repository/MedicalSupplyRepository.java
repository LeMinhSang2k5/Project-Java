package com.java.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.backend.entity.MedicalSupply;

@Repository
public interface MedicalSupplyRepository extends JpaRepository<MedicalSupply, Long> {

}
