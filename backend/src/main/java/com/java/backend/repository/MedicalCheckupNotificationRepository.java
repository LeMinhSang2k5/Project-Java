package com.java.backend.repository;

import com.java.backend.entity.MedicalCheckupNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicalCheckupNotificationRepository extends JpaRepository<MedicalCheckupNotification, Long> {
    List<MedicalCheckupNotification> findByParentId(Long parentId);

    List<MedicalCheckupNotification> findByStudentId(Long studentId);

    List<MedicalCheckupNotification> findByStatus(String status);
}