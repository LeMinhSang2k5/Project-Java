package com.java.backend.service;

import com.java.backend.entity.VaccinationSchedule;
import com.java.backend.enums.ConsentStatus;
import com.java.backend.repository.VaccinationScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VaccinationScheduleServiceTest {

    @Mock
    private VaccinationScheduleRepository repository;

    @Mock
    private com.java.backend.repository.StudentRepository studentRepository;

    @InjectMocks
    private VaccinationScheduleService service;

    private VaccinationSchedule schedule;

    @BeforeEach
    void setUp() {
        schedule = new VaccinationSchedule();
        schedule.setId(1L);
        schedule.setStudentId(10L);
    }

    @Test
    void getAll_Success() {
        when(repository.findAll()).thenReturn(Arrays.asList(schedule));
        List<VaccinationSchedule> result = service.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void getById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(schedule));
        VaccinationSchedule result = service.getById(1L);
        assertNotNull(result);
    }
    
    @Test
    void getById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertNull(service.getById(1L));
    }

    @Test
    void save_Success() {
        schedule.setStudentName("Test Student");
        schedule.setScheduledDateTime(LocalDateTime.now());
        schedule.setVaccineName("COVID-19");
        when(studentRepository.existsById(10L)).thenReturn(true);
        when(repository.save(any(VaccinationSchedule.class))).thenReturn(schedule);
        VaccinationSchedule result = service.save(schedule);
        assertNotNull(result);
        assertNotNull(schedule.getUpdatedDate());
    }

    @Test
    void delete_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(schedule));
        doNothing().when(repository).deleteById(1L);
        service.delete(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void getByStudentId_Success() {
        when(repository.findByStudentId(10L)).thenReturn(Arrays.asList(schedule));
        List<VaccinationSchedule> result = service.getByStudentId(10L);
        assertEquals(1, result.size());
    }

    @Test
    void getPendingParentConsent_Success() {
        when(repository.findPendingParentConsent()).thenReturn(Arrays.asList(schedule));
        List<VaccinationSchedule> result = service.getPendingParentConsent();
        assertEquals(1, result.size());
    }

    @Test
    void getPendingStudentConfirmation_Success() {
        when(repository.findPendingStudentConfirmation()).thenReturn(Arrays.asList(schedule));
        List<VaccinationSchedule> result = service.getPendingStudentConfirmation();
        assertEquals(1, result.size());
    }

    @Test
    void getByParentConsent_Success() {
        when(repository.findByParentConsent(ConsentStatus.APPROVED)).thenReturn(Arrays.asList(schedule));
        List<VaccinationSchedule> result = service.getByParentConsent(ConsentStatus.APPROVED);
        assertEquals(1, result.size());
    }

    @Test
    void getNotVaccinated_Success() {
        when(repository.findByIsVaccinatedFalse()).thenReturn(Arrays.asList(schedule));
        List<VaccinationSchedule> result = service.getNotVaccinated();
        assertEquals(1, result.size());
    }

    @Test
    void getVaccinated_Success() {
        when(repository.findByIsVaccinatedTrue()).thenReturn(Arrays.asList(schedule));
        List<VaccinationSchedule> result = service.getVaccinated();
        assertEquals(1, result.size());
    }

    @Test
    void updateParentConsent_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(schedule));
        when(repository.save(any(VaccinationSchedule.class))).thenReturn(schedule);
        
        VaccinationSchedule result = service.updateParentConsent(1L, ConsentStatus.APPROVED);
        
        assertNotNull(result);
        assertEquals(ConsentStatus.APPROVED, schedule.getParentConsent());
        assertNotNull(schedule.getUpdatedDate());
    }

    @Test
    void updateParentConsent_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(com.java.backend.exception.VaccinationScheduleNotFoundException.class, () -> service.updateParentConsent(1L, ConsentStatus.APPROVED));
    }

    @Test
    void updateStudentConfirmation_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(schedule));
        when(repository.save(any(VaccinationSchedule.class))).thenReturn(schedule);
        
        VaccinationSchedule result = service.updateStudentConfirmation(1L, true);
        
        assertNotNull(result);
        assertTrue(schedule.isStudentConfirmation());
        assertNotNull(schedule.getUpdatedDate());
    }

    @Test
    void updateStudentConfirmation_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(com.java.backend.exception.VaccinationScheduleNotFoundException.class, () -> service.updateStudentConfirmation(1L, true));
    }

    @Test
    void updateVaccinationStatus_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(schedule));
        when(repository.save(any(VaccinationSchedule.class))).thenReturn(schedule);
        
        VaccinationSchedule result = service.updateVaccinationStatus(1L, true);
        
        assertNotNull(result);
        assertTrue(schedule.isVaccinated());
        assertNotNull(schedule.getUpdatedDate());
    }

    @Test
    void updateVaccinationStatus_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(com.java.backend.exception.VaccinationScheduleNotFoundException.class, () -> service.updateVaccinationStatus(1L, true));
    }

    @Test
    void getByDateRange_Success() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        when(repository.findByScheduledDateTimeBetween(start, end)).thenReturn(Arrays.asList(schedule));
        
        List<VaccinationSchedule> result = service.getByDateRange(start, end);
        assertEquals(1, result.size());
    }

    @Test
    void getPendingParentConsentByParentId_Success() {
        when(repository.findPendingParentConsentByParentId(100L)).thenReturn(Arrays.asList(schedule));
        List<VaccinationSchedule> result = service.getPendingParentConsentByParentId(100L);
        assertEquals(1, result.size());
    }

    @Test
    void validateSchedule_StudentIdNull() {
        schedule.setStudentId(null);
        assertThrows(IllegalArgumentException.class, () -> service.validateSchedule(schedule));
    }

    @Test
    void validateSchedule_StudentNotFound() {
        when(studentRepository.existsById(10L)).thenReturn(false);
        assertThrows(com.java.backend.exception.StudentNotFoundException.class, () -> service.validateSchedule(schedule));
    }

    @Test
    void validateSchedule_VaccineNameEmpty() {
        when(studentRepository.existsById(10L)).thenReturn(true);
        schedule.setVaccineName("");
        assertThrows(IllegalArgumentException.class, () -> service.validateSchedule(schedule));
    }

    @Test
    void validateSchedule_ScheduledDateTimeNull() {
        when(studentRepository.existsById(10L)).thenReturn(true);
        schedule.setVaccineName("COVID-19");
        schedule.setScheduledDateTime(null);
        assertThrows(IllegalArgumentException.class, () -> service.validateSchedule(schedule));
    }

    @Test
    void validateSchedule_StudentNameEmpty() {
        when(studentRepository.existsById(10L)).thenReturn(true);
        schedule.setVaccineName("COVID-19");
        schedule.setScheduledDateTime(LocalDateTime.now());
        schedule.setStudentName("   ");
        assertThrows(IllegalArgumentException.class, () -> service.validateSchedule(schedule));
    }

    @Test
    void validateDateRange_NullDates() {
        assertThrows(IllegalArgumentException.class, () -> service.validateDateRange(null, LocalDateTime.now()));
        assertThrows(IllegalArgumentException.class, () -> service.validateDateRange(LocalDateTime.now(), null));
    }

    @Test
    void validateDateRange_StartAfterEnd() {
        assertThrows(IllegalArgumentException.class, () -> service.validateDateRange(LocalDateTime.now().plusDays(1), LocalDateTime.now()));
    }

    @Test
    void parseParentConsent_Empty() {
        assertThrows(IllegalArgumentException.class, () -> service.parseParentConsent(null));
        assertThrows(IllegalArgumentException.class, () -> service.parseParentConsent("   "));
    }

    @Test
    void parseParentConsent_Invalid() {
        assertThrows(IllegalArgumentException.class, () -> service.parseParentConsent("INVALID_CONSENT"));
    }

    @Test
    void parseParentConsent_Valid() {
        ConsentStatus status = service.parseParentConsent("approved");
        assertEquals(ConsentStatus.APPROVED, status);
    }

    @Test
    void validateBooleanField_Null() {
        assertThrows(IllegalArgumentException.class, () -> service.validateBooleanField(null, "TestField"));
    }

    @Test
    void validateBooleanField_Valid() {
        assertDoesNotThrow(() -> service.validateBooleanField(true, "TestField"));
    }

    @Test
    void save_CreatedDateNull() {
        schedule.setStudentName("Test Student");
        schedule.setScheduledDateTime(LocalDateTime.now());
        schedule.setVaccineName("COVID-19");
        schedule.setCreatedDate(null);
        when(studentRepository.existsById(10L)).thenReturn(true);
        when(repository.save(any(VaccinationSchedule.class))).thenReturn(schedule);
        VaccinationSchedule result = service.save(schedule);
        assertNotNull(result);
        assertNotNull(schedule.getCreatedDate());
    }

    @Test
    void saveWithoutValidation_Success() {
        when(repository.save(any(VaccinationSchedule.class))).thenReturn(schedule);
        VaccinationSchedule result = service.saveWithoutValidation(schedule);
        assertNotNull(result);
        assertNotNull(schedule.getUpdatedDate());
    }

    @Test
    void createFromStudent_StudentNotFound() {
        when(studentRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(com.java.backend.exception.StudentNotFoundException.class, () -> 
            service.createFromStudent(10L, "COVID", LocalDateTime.now(), "Notes", "Clinic"));
    }

    @Test
    void createFromStudent_Success() {
        com.java.backend.entity.Student student = new com.java.backend.entity.Student();
        student.setId(10L);
        student.setFullName("John Doe");
        student.setCode("STU001");
        
        when(studentRepository.findById(10L)).thenReturn(Optional.of(student));
        when(studentRepository.existsById(10L)).thenReturn(true);
        when(repository.save(any(VaccinationSchedule.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VaccinationSchedule result = service.createFromStudent(10L, "COVID", LocalDateTime.now(), "Notes", "Clinic");
        
        assertNotNull(result);
        assertEquals("John Doe", result.getStudentName());
        assertEquals("STU001", result.getStudentCode());
        assertEquals("COVID", result.getVaccineName());
        assertEquals(ConsentStatus.PENDING, result.getParentConsent());
        assertFalse(result.isVaccinated());
        assertFalse(result.isStudentConfirmation());
    }

    @Test
    void delete_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(com.java.backend.exception.VaccinationScheduleNotFoundException.class, () -> service.delete(1L));
    }
}
