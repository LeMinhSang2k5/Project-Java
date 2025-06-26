package com.java.backend.repository;

import com.java.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Spring Data JPA automatically provides basic CRUD operations
    // Custom query methods can be added here if needed

    Optional<Student> findByCode(String code);
}
