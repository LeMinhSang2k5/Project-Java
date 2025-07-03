package com.java.backend.service;

import com.java.backend.entity.MedicalCheckupNotification;
import com.java.backend.entity.MedicalCheckupResult;
import com.java.backend.repository.MedicalCheckupNotificationRepository;
import com.java.backend.repository.MedicalCheckupResultRepository;
import com.java.backend.repository.StudentRepository;
import com.java.backend.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class MedicalCheckupService {
    @Autowired
    private MedicalCheckupNotificationRepository notificationRepo;
    @Autowired
    private MedicalCheckupResultRepository resultRepo;
    @Autowired
    private StudentRepository studentRepo;

    // Notification
    public MedicalCheckupNotification saveNotification(MedicalCheckupNotification n) {
        return notificationRepo.save(n);
    }

    public List<MedicalCheckupNotification> getNotificationsByParent(Long parentId) {
        return notificationRepo.findByParentId(parentId);
    }

    public List<MedicalCheckupNotification> getNotificationsByStudent(Long studentId) {
        return notificationRepo.findByStudentId(studentId);
    }

    public List<MedicalCheckupNotification> getNotificationsByStatus(String status) {
        return notificationRepo.findByStatus(status);
    }

    public MedicalCheckupNotification getNotification(Long id) {
        return notificationRepo.findById(id).orElse(null);
    }

    public List<MedicalCheckupNotification> getAllNotifications() {
        return notificationRepo.findAll();
    }

    // Result
    public MedicalCheckupResult saveResult(MedicalCheckupResult r) {
        return resultRepo.save(r);
    }

    public List<MedicalCheckupResult> getResultsByStudent(Long studentId) {
        return resultRepo.findByStudentId(studentId);
    }

    public MedicalCheckupResult getResult(Long id) {
        return resultRepo.findById(id).orElse(null);
    }

    public List<MedicalCheckupResult> getResultsByParent(Long parentId) {
        List<Student> students = studentRepo.findByParentId(parentId);
        List<MedicalCheckupResult> results = new ArrayList<>();
        for (Student s : students) {
            results.addAll(resultRepo.findByStudentId(s.getId()));
        }
        return results;
    }
}