package com.java.backend.service;

import com.java.backend.entity.MedicalCheckupNotification;
import com.java.backend.entity.MedicalCheckupResult;
import com.java.backend.entity.Student;
import com.java.backend.repository.MedicalCheckupNotificationRepository;
import com.java.backend.repository.MedicalCheckupResultRepository;
import com.java.backend.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalCheckupServiceTest {

    @Mock
    private MedicalCheckupNotificationRepository notificationRepo;

    @Mock
    private MedicalCheckupResultRepository resultRepo;

    @Mock
    private StudentRepository studentRepo;

    @Mock
    private com.java.backend.repository.ParentRepository parentRepository;

    @InjectMocks
    private MedicalCheckupService service;

    private MedicalCheckupNotification notification;
    private MedicalCheckupResult result;
    private Student student;

    @BeforeEach
    void setUp() {
        notification = new MedicalCheckupNotification();
        notification.setStudentId(1L);
        notification.setParentId(1L);
        notification.setScheduledDate(java.time.LocalDateTime.now());
        notification.setContent("Test content");
        
        result = new MedicalCheckupResult();
        result.setId(1L);
        result.setStudentId(1L);
        result.setCheckupDate(java.time.LocalDateTime.now());
        result.setResult("Good");
        
        student = new Student();
        student.setId(1L);
    }

    // --- Notification Tests ---

    @Test
    void saveNotification_Success() {
        when(studentRepo.existsById(1L)).thenReturn(true);
        when(parentRepository.existsById(1L)).thenReturn(true);
        when(notificationRepo.save(any(MedicalCheckupNotification.class))).thenReturn(notification);
        MedicalCheckupNotification saved = service.saveNotification(notification);
        assertNotNull(saved);
    }

    @Test
    void getNotificationsByParent_Success() {
        when(notificationRepo.findByParentId(1L)).thenReturn(Arrays.asList(notification));
        List<MedicalCheckupNotification> list = service.getNotificationsByParent(1L);
        assertEquals(1, list.size());
    }

    @Test
    void getNotificationsByStudent_Success() {
        when(notificationRepo.findByStudentId(1L)).thenReturn(Arrays.asList(notification));
        List<MedicalCheckupNotification> list = service.getNotificationsByStudent(1L);
        assertEquals(1, list.size());
    }

    @Test
    void getNotificationsByStatus_Success() {
        when(notificationRepo.findByStatus("PENDING")).thenReturn(Arrays.asList(notification));
        List<MedicalCheckupNotification> list = service.getNotificationsByStatus("PENDING");
        assertEquals(1, list.size());
    }

    @Test
    void getNotification_Found() {
        when(notificationRepo.findById(1L)).thenReturn(Optional.of(notification));
        MedicalCheckupNotification found = service.getNotification(1L);
        assertNotNull(found);
    }

    @Test
    void getNotification_NotFound() {
        when(notificationRepo.findById(1L)).thenReturn(Optional.empty());
        MedicalCheckupNotification found = service.getNotification(1L);
        assertNull(found);
    }

    @Test
    void getAllNotifications_Success() {
        when(notificationRepo.findAll()).thenReturn(Arrays.asList(notification));
        List<MedicalCheckupNotification> list = service.getAllNotifications();
        assertEquals(1, list.size());
    }

    // --- Result Tests ---

    @Test
    void saveResult_Success() {
        when(studentRepo.existsById(1L)).thenReturn(true);
        when(resultRepo.save(any(MedicalCheckupResult.class))).thenReturn(result);
        MedicalCheckupResult saved = service.saveResult(result);
        assertNotNull(saved);
    }

    @Test
    void getResultsByStudent_Success() {
        when(resultRepo.findByStudentId(1L)).thenReturn(Arrays.asList(result));
        List<MedicalCheckupResult> list = service.getResultsByStudent(1L);
        assertEquals(1, list.size());
    }

    @Test
    void getResult_Found() {
        when(resultRepo.findById(1L)).thenReturn(Optional.of(result));
        MedicalCheckupResult found = service.getResult(1L);
        assertNotNull(found);
    }

    @Test
    void getResult_NotFound() {
        when(resultRepo.findById(1L)).thenReturn(Optional.empty());
        MedicalCheckupResult found = service.getResult(1L);
        assertNull(found);
    }

    @Test
    void getResultsByParent_WithStudents() {
        when(studentRepo.findByParentId(1L)).thenReturn(Arrays.asList(student));
        when(resultRepo.findByStudentId(1L)).thenReturn(Arrays.asList(result));
        
        List<MedicalCheckupResult> list = service.getResultsByParent(1L);
        assertEquals(1, list.size());
    }

    @Test
    void getResultsByParent_NoStudents() {
        when(studentRepo.findByParentId(1L)).thenReturn(Collections.emptyList());
        
        List<MedicalCheckupResult> list = service.getResultsByParent(1L);
        assertEquals(0, list.size());
    }

    // --- Validation Tests ---

    @Test
    void validateNotification_StudentIdNull() {
        notification.setStudentId(null);
        assertThrows(IllegalArgumentException.class, () -> service.validateNotification(notification));
    }

    @Test
    void validateNotification_ParentIdNull() {
        notification.setParentId(null);
        assertThrows(IllegalArgumentException.class, () -> service.validateNotification(notification));
    }

    @Test
    void validateNotification_ScheduledDateNull() {
        notification.setScheduledDate(null);
        assertThrows(IllegalArgumentException.class, () -> service.validateNotification(notification));
    }

    @Test
    void validateNotification_ContentEmpty() {
        notification.setContent("  ");
        assertThrows(IllegalArgumentException.class, () -> service.validateNotification(notification));
    }

    @Test
    void validateNotification_StudentNotFound() {
        when(studentRepo.existsById(1L)).thenReturn(false);
        assertThrows(com.java.backend.exception.StudentNotFoundException.class, () -> service.validateNotification(notification));
    }

    @Test
    void validateNotification_ParentNotFound() {
        when(studentRepo.existsById(1L)).thenReturn(true);
        when(parentRepository.existsById(1L)).thenReturn(false);
        assertThrows(com.java.backend.exception.ParentNotFoundException.class, () -> service.validateNotification(notification));
    }

    @Test
    void validateConsentStatus_NullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> service.validateConsentStatus(null));
        assertThrows(IllegalArgumentException.class, () -> service.validateConsentStatus("   "));
    }

    @Test
    void validateConsentStatus_Invalid() {
        assertThrows(IllegalArgumentException.class, () -> service.validateConsentStatus("INVALID"));
    }

    @Test
    void validateConsentStatus_Valid() {
        assertDoesNotThrow(() -> service.validateConsentStatus("approved"));
        assertDoesNotThrow(() -> service.validateConsentStatus("REJECTED"));
    }

    @Test
    void validateResult_StudentIdNull() {
        result.setStudentId(null);
        assertThrows(IllegalArgumentException.class, () -> service.validateResult(result));
    }

    @Test
    void validateResult_CheckupDateNull() {
        result.setCheckupDate(null);
        assertThrows(IllegalArgumentException.class, () -> service.validateResult(result));
    }

    @Test
    void validateResult_ResultEmpty() {
        result.setResult("   ");
        assertThrows(IllegalArgumentException.class, () -> service.validateResult(result));
    }

    @Test
    void validateResult_StudentNotFound() {
        when(studentRepo.existsById(1L)).thenReturn(false);
        assertThrows(com.java.backend.exception.StudentNotFoundException.class, () -> service.validateResult(result));
    }

    @Test
    void getNotificationOrThrow_Found() {
        when(notificationRepo.findById(1L)).thenReturn(Optional.of(notification));
        MedicalCheckupNotification found = service.getNotificationOrThrow(1L);
        assertNotNull(found);
    }

    @Test
    void getNotificationOrThrow_NotFound() {
        when(notificationRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(com.java.backend.exception.MedicalCheckupNotificationNotFoundException.class, () -> service.getNotificationOrThrow(1L));
    }

    @Test
    void getResultOrThrow_Found() {
        when(resultRepo.findById(1L)).thenReturn(Optional.of(result));
        MedicalCheckupResult found = service.getResultOrThrow(1L);
        assertNotNull(found);
    }

    @Test
    void getResultOrThrow_NotFound() {
        when(resultRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(com.java.backend.exception.MedicalCheckupResultNotFoundException.class, () -> service.getResultOrThrow(1L));
    }
}
