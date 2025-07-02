package com.java.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.backend.entity.SchoolHealthDoc;

@Repository
public interface SchoolHealthDocRepository extends JpaRepository<SchoolHealthDoc, Long> {

}
