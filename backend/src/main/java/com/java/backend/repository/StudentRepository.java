package com.java.backend.repository;

import com.java.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByCode(String code);

    List<Student> findByParentId(Long parentId);
}
