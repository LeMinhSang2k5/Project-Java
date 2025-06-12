package com.java.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.java.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}