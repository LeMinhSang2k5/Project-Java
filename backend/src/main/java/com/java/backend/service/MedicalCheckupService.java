package com.java.backend.service;

import com.java.backend.entity.MedicalCheckupNotification;
import com.java.backend.entity.MedicalCheckupResult;
import com.java.backend.entity.Student;
import com.java.backend.exception.MedicalCheckupNotificationNotFoundException;
import com.java.backend.exception.MedicalCheckupResultNotFoundException;
import com.java.backend.exception.ParentNotFoundException;
import com.java.backend.exception.StudentNotFoundException;
import com.java.backend.repository.MedicalCheckupNotificationRepository;
import com.java.backend.repository.MedicalCheckupResultRepository;
import com.java.backend.repository.ParentRepository;
import com.java.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MedicalCheckupService {

    private static final Set<String> VALID_CONSENT_UPDATE_STATUSES = Set.of("APPROVED", "REJECTED");

    @Autowired
    private MedicalCheckupNotificationRepository notificationRepo;
    @Autowired
    private MedicalCheckupResultRepository resultRepo;
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private ParentRepository parentRepository;

    public MedicalCheckupNotification saveNotification(MedicalCheckupNotification notification) {
        validateNotification(notification);
        return notificationRepo.save(notification);
    }

    public void validateNotification(MedicalCheckupNotification notification) {
        if (notification.getStudentId() == null) {
            throw new IllegalArgumentException("Phải cung cấp ID học sinh");
        }
        if (notification.getParentId() == null) {
            throw new IllegalArgumentException("Phải cung cấp ID phụ huynh");
        }
        if (notification.getScheduledDate() == null) {
            throw new IllegalArgumentException("Ngày khám dự kiến không được để trống");
        }
        if (notification.getContent() == null || notification.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Nội dung thông báo không được để trống");
        }

        if (!studentRepo.existsById(notification.getStudentId())) {
            throw new StudentNotFoundException(notification.getStudentId());
        }
        if (!parentRepository.existsById(notification.getParentId())) {
            throw new ParentNotFoundException(notification.getParentId());
        }
    }

    public void validateConsentStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Giá trị đồng ý không được để trống");
        }
        String normalized = status.trim().toUpperCase();
        if (!VALID_CONSENT_UPDATE_STATUSES.contains(normalized)) {
            throw new IllegalArgumentException("Giá trị đồng ý không hợp lệ");
        }
    }

    public void validateResult(MedicalCheckupResult result) {
        if (result.getStudentId() == null) {
            throw new IllegalArgumentException("Phải cung cấp ID học sinh");
        }
        if (result.getCheckupDate() == null) {
            throw new IllegalArgumentException("Ngày khám không được để trống");
        }
        if (result.getResult() == null || result.getResult().trim().isEmpty()) {
            throw new IllegalArgumentException("Kết quả khám không được để trống");
        }
        if (!studentRepo.existsById(result.getStudentId())) {
            throw new StudentNotFoundException(result.getStudentId());
        }
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

    public MedicalCheckupNotification getNotificationOrThrow(Long id) {
        return notificationRepo.findById(id)
                .orElseThrow(() -> new MedicalCheckupNotificationNotFoundException(id));
    }

    public List<MedicalCheckupNotification> getAllNotifications() {
        return notificationRepo.findAll();
    }

    public MedicalCheckupResult saveResult(MedicalCheckupResult result) {
        validateResult(result);
        return resultRepo.save(result);
    }

    public List<MedicalCheckupResult> getResultsByStudent(Long studentId) {
        return resultRepo.findByStudentId(studentId);
    }

    public MedicalCheckupResult getResult(Long id) {
        return resultRepo.findById(id).orElse(null);
    }

    public MedicalCheckupResult getResultOrThrow(Long id) {
        return resultRepo.findById(id)
                .orElseThrow(() -> new MedicalCheckupResultNotFoundException(id));
    }

    public List<MedicalCheckupResult> getResultsByParent(Long parentId) {
        List<Student> students = studentRepo.findByParentId(parentId);
        List<MedicalCheckupResult> results = new ArrayList<>();
        for (Student student : students) {
            results.addAll(resultRepo.findByStudentId(student.getId()));
        }
        return results;
    }
}
