package com.java.backend.repository;

import com.java.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Spring Data JPA automatically provides basic CRUD operations
    // You can add custom query methods here if needed
}
