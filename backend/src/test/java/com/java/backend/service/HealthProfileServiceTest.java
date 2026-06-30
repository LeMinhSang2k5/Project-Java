package com.java.backend.service;

import com.java.backend.entity.HealthProfile;
import com.java.backend.entity.Student;
import com.java.backend.enums.Gender;
import com.java.backend.repository.HealthProfileRepository;
import com.java.backend.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HealthProfileServiceTest {

    @Mock
    private HealthProfileRepository healthProfileRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private HealthProfileService healthProfileService;

    private HealthProfile healthProfile;
    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setFullName("Test Student");

        healthProfile = new HealthProfile();
        healthProfile.setId(1L);
        healthProfile.setStudent(student);
    }

    @Test
    void saveHealthProfile_Success() {
        healthProfile.setStudentName("New Name");
        healthProfile.setDateOfBirth(LocalDate.of(2010, 1, 1));
        healthProfile.setGender("MALE");
        healthProfile.setClassName("10A1");
        healthProfile.setCity("Hanoi");
        healthProfile.setDistrict("Cau Giay");
        healthProfile.setGrade("10");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(healthProfileRepository.save(any(HealthProfile.class))).thenReturn(healthProfile);

        HealthProfile savedProfile = healthProfileService.saveHealthProfile(healthProfile);

        assertNotNull(savedProfile);
        assertEquals("New Name", student.getFullName());
        assertEquals(Gender.MALE, student.getGender());
        verify(studentRepository).save(student);
        verify(healthProfileRepository).save(healthProfile);
    }

    @Test
    void saveHealthProfile_StudentNull() {
        HealthProfile profile = new HealthProfile();
        Exception exception = assertThrows(RuntimeException.class, () -> healthProfileService.saveHealthProfile(profile));
        assertEquals("Phải cung cấp ID học sinh khi tạo hồ sơ sức khỏe", exception.getMessage());
    }

    @Test
    void saveHealthProfile_StudentIdNull() {
        HealthProfile profile = new HealthProfile();
        profile.setStudent(new Student());
        Exception exception = assertThrows(RuntimeException.class, () -> healthProfileService.saveHealthProfile(profile));
        assertEquals("Phải cung cấp ID học sinh khi tạo hồ sơ sức khỏe", exception.getMessage());
    }

    @Test
    void saveHealthProfile_StudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> healthProfileService.saveHealthProfile(healthProfile));
        assertEquals("Không tìm thấy học sinh với ID: 1", exception.getMessage());
    }
    
    @Test
    void saveHealthProfile_InvalidGender() {
        healthProfile.setGender("INVALID_GENDER");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(healthProfileRepository.save(any(HealthProfile.class))).thenReturn(healthProfile);
        
        healthProfileService.saveHealthProfile(healthProfile);
        // Gender parse fail should not throw exception, student gender remains unchanged (null)
        assertNull(student.getGender());
    }

    @Test
    void getAllHealthProfiles_Success() {
        when(healthProfileRepository.findAll()).thenReturn(Arrays.asList(healthProfile));
        List<HealthProfile> profiles = healthProfileService.getAllHealthProfiles();
        assertEquals(1, profiles.size());
    }

    @Test
    void getHealthProfileById_Success() {
        when(healthProfileRepository.findById(1L)).thenReturn(Optional.of(healthProfile));
        HealthProfile profile = healthProfileService.getHealthProfileById(1L);
        assertNotNull(profile);
    }

    @Test
    void getHealthProfileById_NotFound() {
        when(healthProfileRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> healthProfileService.getHealthProfileById(1L));
    }

    @Test
    void updateHealthProfile_Success() {
        HealthProfile updateData = new HealthProfile();
        updateData.setId(1L);
        updateData.setStudentName("Updated Name");
        updateData.setClassName("11A1");
        updateData.setGrade("11");
        updateData.setGender("FEMALE");
        updateData.setDateOfBirth(LocalDate.of(2009, 1, 1));
        updateData.setCity("HCM");
        updateData.setDistrict("Q1");
        updateData.setAllergies("None");
        updateData.setChronicDiseases("None");
        updateData.setMedicalHistory("None");
        updateData.setVaccinationHistory("Done");
        updateData.setVisionDetails("10/10");
        updateData.setHearingDetails("Good");
        updateData.setStudent(student);

        when(healthProfileRepository.existsById(1L)).thenReturn(true);
        when(healthProfileRepository.findById(1L)).thenReturn(Optional.of(healthProfile));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(healthProfileRepository.save(any(HealthProfile.class))).thenReturn(healthProfile);

        HealthProfile updated = healthProfileService.updateHealthProfile(updateData);
        assertNotNull(updated);
        assertEquals("Updated Name", student.getFullName());
    }
    
    @Test
    void updateHealthProfile_StudentIdNull() {
        HealthProfile updateData = new HealthProfile();
        updateData.setId(1L);
        updateData.setStudent(new Student()); // Student with null ID
        
        when(healthProfileRepository.existsById(1L)).thenReturn(true);
        when(healthProfileRepository.findById(1L)).thenReturn(Optional.of(healthProfile));
        when(healthProfileRepository.save(any(HealthProfile.class))).thenReturn(healthProfile);

        HealthProfile updated = healthProfileService.updateHealthProfile(updateData);
        assertNotNull(updated);
    }

    @Test
    void updateHealthProfile_NotExists() {
        HealthProfile updateData = new HealthProfile();
        updateData.setId(1L);
        when(healthProfileRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> healthProfileService.updateHealthProfile(updateData));
    }

    @Test
    void updateHealthProfile_ExistsButFindByIdReturnsEmpty() {
        HealthProfile updateData = new HealthProfile();
        updateData.setId(1L);
        when(healthProfileRepository.existsById(1L)).thenReturn(true);
        when(healthProfileRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> healthProfileService.updateHealthProfile(updateData));
    }

    @Test
    void updateHealthProfile_StudentNotFound() {
        HealthProfile updateData = new HealthProfile();
        updateData.setId(1L);
        updateData.setStudent(student);
        when(healthProfileRepository.existsById(1L)).thenReturn(true);
        when(healthProfileRepository.findById(1L)).thenReturn(Optional.of(healthProfile));
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> healthProfileService.updateHealthProfile(updateData));
    }

    @Test
    void deleteHealthProfile_Success() {
        when(healthProfileRepository.existsById(1L)).thenReturn(true);
        doNothing().when(healthProfileRepository).deleteById(1L);
        healthProfileService.deleteHealthProfile(1L);
        verify(healthProfileRepository).deleteById(1L);
    }

    @Test
    void findByStudentIdOrThrow_Success() {
        when(healthProfileRepository.findByStudent_Id(1L)).thenReturn(Optional.of(healthProfile));
        HealthProfile result = healthProfileService.findByStudentIdOrThrow(1L);
        assertNotNull(result);
    }
}
