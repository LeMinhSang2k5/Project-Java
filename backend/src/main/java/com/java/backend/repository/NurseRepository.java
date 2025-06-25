package com.java.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.backend.entity.Nurse;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {

}
