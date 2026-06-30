package com.java.backend.service;

import com.java.backend.entity.Medical;
import com.java.backend.entity.Parent;
import com.java.backend.entity.Student;
import com.java.backend.repository.MedicalRepository;
import com.java.backend.repository.ParentRepository;
import com.java.backend.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalServiceTest {

    @Mock
    private MedicalRepository medicalRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ParentRepository parentRepository;

    @InjectMocks
    private MedicalService medicalService;

    private Student student;
    private Parent parent;
    private Medical medical;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);

        parent = new Parent();
        parent.setId(1L);

        medical = new Medical(student, parent, "Aspirin", "1 pill", "Take after meal");
        medical.setId(1L);
    }

    @Test
    void createMedicalRequest_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(parentRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(medicalRepository.save(any(Medical.class))).thenReturn(medical);

        Medical result = medicalService.createMedicalRequest(1L, 1L, "Aspirin", "1 pill", "Take after meal");
        assertNotNull(result);
        assertEquals("Aspirin", result.getMedicalName());
    }

    @Test
    void createMedicalRequest_StudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            medicalService.createMedicalRequest(1L, 1L, "Aspirin", "1 pill", "Take after meal");
        });
        assertTrue(exception.getMessage().contains("Student not found"));
    }

    @Test
    void createMedicalRequest_ParentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(parentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            medicalService.createMedicalRequest(1L, 1L, "Aspirin", "1 pill", "Take after meal");
        });
        assertTrue(exception.getMessage().contains("Parent not found"));
    }

    @Test
    void getAllMedicalRequests_Success() {
        when(medicalRepository.findAll()).thenReturn(Arrays.asList(medical));
        List<Medical> result = medicalService.getAllMedicalRequests();
        assertEquals(1, result.size());
    }

    @Test
    void getMedicalRequestById_Success() {
        when(medicalRepository.findById(1L)).thenReturn(Optional.of(medical));
        Medical result = medicalService.getMedicalRequestById(1L);
        assertNotNull(result);
    }

    @Test
    void getMedicalRequestById_NotFound() {
        when(medicalRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            medicalService.getMedicalRequestById(1L);
        });
        assertTrue(exception.getMessage().contains("Medical request not found"));
    }

    @Test
    void getMedicalRequestsByStudentId_Success() {
        when(medicalRepository.findByStudentIdOrderByRequestDateDesc(1L)).thenReturn(Arrays.asList(medical));
        List<Medical> result = medicalService.getMedicalRequestsByStudentId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void getMedicalRequestsByParentId_Success() {
        when(medicalRepository.findByParentIdOrderByRequestDateDesc(1L)).thenReturn(Arrays.asList(medical));
        List<Medical> result = medicalService.getMedicalRequestsByParentId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void getMedicalRequestsByStatus_Success() {
        when(medicalRepository.findByStatusOrderByRequestDateDesc("PENDING")).thenReturn(Arrays.asList(medical));
        List<Medical> result = medicalService.getMedicalRequestsByStatus("PENDING");
        assertEquals(1, result.size());
    }

    @Test
    void getMedicalRequestsByStudentIdAndStatus_Success() {
        when(medicalRepository.findByStudentIdAndStatusOrderByRequestDateDesc(1L, "PENDING")).thenReturn(Arrays.asList(medical));
        List<Medical> result = medicalService.getMedicalRequestsByStudentIdAndStatus(1L, "PENDING");
        assertEquals(1, result.size());
    }

    @Test
    void updateMedicalRequestStatus_Approved() {
        when(medicalRepository.findById(1L)).thenReturn(Optional.of(medical));
        when(medicalRepository.save(any(Medical.class))).thenReturn(medical);

        Medical result = medicalService.updateMedicalRequestStatus(1L, "APPROVED", "Nurse A");
        
        assertEquals("APPROVED", result.getStatus());
        assertEquals("Nurse A", result.getApprovedBy());
        assertNotNull(result.getApprovedDate());
    }

    @Test
    void updateMedicalRequestStatus_Completed() {
        when(medicalRepository.findById(1L)).thenReturn(Optional.of(medical));
        when(medicalRepository.save(any(Medical.class))).thenReturn(medical);

        Medical result = medicalService.updateMedicalRequestStatus(1L, "COMPLETED", "Nurse B");
        
        assertEquals("COMPLETED", result.getStatus());
        assertEquals("Nurse B", result.getAdministeredBy());
        assertNotNull(result.getAdministeredDate());
    }
    
    @Test
    void updateMedicalRequestStatus_OtherStatus() {
        when(medicalRepository.findById(1L)).thenReturn(Optional.of(medical));
        when(medicalRepository.save(any(Medical.class))).thenReturn(medical);

        Medical result = medicalService.updateMedicalRequestStatus(1L, "REJECTED", "Nurse C");
        
        assertEquals("REJECTED", result.getStatus());
        assertNull(result.getApprovedBy()); // not set by this block
    }

    @Test
    void updateMedicalRequest_Success() {
        when(medicalRepository.findById(1L)).thenReturn(Optional.of(medical));
        when(medicalRepository.save(any(Medical.class))).thenReturn(medical);

        Medical result = medicalService.updateMedicalRequest(1L, "Panadol", "2 pills", "Morning");
        
        assertEquals("Panadol", result.getMedicalName());
        assertEquals("2 pills", result.getDosage());
        assertEquals("Morning", result.getNote());
    }

    @Test
    void deleteMedicalRequest_Success() {
        when(medicalRepository.existsById(1L)).thenReturn(true);
        doNothing().when(medicalRepository).deleteById(1L);

        medicalService.deleteMedicalRequest(1L);
        verify(medicalRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMedicalRequest_NotFound() {
        when(medicalRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            medicalService.deleteMedicalRequest(1L);
        });
        assertTrue(exception.getMessage().contains("Medical request not found"));
    }
}
