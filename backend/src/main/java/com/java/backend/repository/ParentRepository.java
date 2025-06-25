package com.java.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.backend.entity.Parent;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    // Spring Data JPA automatically provides basic CRUD operations
    // You can add custom query methods here if needed

}
