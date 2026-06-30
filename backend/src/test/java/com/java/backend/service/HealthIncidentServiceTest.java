package com.java.backend.service;

import com.java.backend.entity.HealthIncident;
import com.java.backend.entity.Student;
import com.java.backend.entity.User;
import com.java.backend.repository.HealthIncidentRepository;
import com.java.backend.repository.StudentRepository;
import com.java.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HealthIncidentServiceTest {

    @Mock
    private HealthIncidentRepository healthIncidentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private com.java.backend.repository.IncidentSupplyUsageRepository incidentSupplyUsageRepository;

    @InjectMocks
    private HealthIncidentService healthIncidentService;

    @Test
    void createHealthIncident_shouldAssignRelatedEntitiesAndSave() {
        Student student = new Student();
        student.setId(1L);
        User user = new User();
        user.setId(2L);
        HealthIncident incident = new HealthIncident();
        incident.setStudent(new Student());
        incident.getStudent().setId(1L);
        incident.setReportedBy(new User());
        incident.getReportedBy().setId(2L);
        incident.setIncidentType("FALL");
        incident.setDescription("Minor injury");
        incident.setStatus(HealthIncident.IncidentStatus.REPORTED);
        incident.setIncidentTime(LocalDateTime.now());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(healthIncidentRepository.save(any(HealthIncident.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        HealthIncident result = healthIncidentService.createHealthIncident(incident);

        assertSame(student, result.getStudent());
        assertSame(user, result.getReportedBy());
        verify(healthIncidentRepository).save(incident);
    }

    @Test
    void createHealthIncident_shouldThrowWhenStudentMissing() {
        HealthIncident incident = new HealthIncident();
        incident.setStudent(new Student());
        incident.getStudent().setId(99L);
        incident.setReportedBy(new User());
        incident.getReportedBy().setId(2L);
        incident.setDescription("Test");

        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> healthIncidentService.createHealthIncident(incident));

        assertTrue(exception.getMessage().contains("ID"));
        verify(healthIncidentRepository, never()).save(any());
    }

    @Test
    void createHealthIncident_shouldThrowWhenUserMissing() {
        HealthIncident incident = new HealthIncident();
        incident.setStudent(new Student());
        incident.getStudent().setId(1L);
        incident.setReportedBy(new User());
        incident.getReportedBy().setId(99L);
        incident.setDescription("Test");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> healthIncidentService.createHealthIncident(incident));
    }

    @Test
    void getHealthIncidentById_shouldReturnIncident() {
        HealthIncident existing = new HealthIncident();
        when(healthIncidentRepository.findById(1L)).thenReturn(Optional.of(existing));
        HealthIncident result = healthIncidentService.getHealthIncidentById(1L);
        assertNotNull(result);
    }

    @Test
    void getHealthIncidentById_shouldThrowWhenNotFound() {
        when(healthIncidentRepository.findById(77L)).thenThrow(new com.java.backend.exception.HealthIncidentNotFoundException(77L));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> healthIncidentService.getHealthIncidentById(77L));
        assertTrue(exception.getMessage().contains("y tế"));
    }

    @Test
    void getAllHealthIncidents_shouldReturnList() {
        when(healthIncidentRepository.findAll()).thenReturn(List.of(new HealthIncident()));
        var list = healthIncidentService.getAllHealthIncidents();
        assertFalse(list.isEmpty());
    }

    @Test
    void updateHealthIncident_shouldUpdateExistingIncidentAndSave() {
        HealthIncident existing = new HealthIncident();
        existing.setId(5L);
        existing.setIncidentType("FEVER");
        existing.setDescription("Old description");
        existing.setActionTaken("Old action");
        existing.setIncidentTime(LocalDateTime.of(2024, 1, 1, 8, 0));
        existing.setStatus(HealthIncident.IncidentStatus.MONITORING);

        HealthIncident updatePayload = new HealthIncident();
        updatePayload.setIncidentType("HEADACHE");
        updatePayload.setDescription("New description");
        updatePayload.setActionTaken("New action");
        updatePayload.setIncidentTime(LocalDateTime.of(2024, 1, 1, 9, 0));
        updatePayload.setStatus(HealthIncident.IncidentStatus.RESOLVED);
        Student student = new Student();
        student.setId(11L);
        updatePayload.setStudent(student);
        User user = new User();
        user.setId(22L);
        updatePayload.setReportedBy(user);

        when(healthIncidentRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(studentRepository.findById(11L)).thenReturn(Optional.of(new Student()));
        when(userRepository.findById(22L)).thenReturn(Optional.of(new User()));
        when(healthIncidentRepository.save(any(HealthIncident.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        HealthIncident result = healthIncidentService.updateHealthIncident(5L, updatePayload);

        assertEquals("HEADACHE", result.getIncidentType());
        assertEquals("New description", result.getDescription());
        assertEquals("New action", result.getActionTaken());
        assertEquals(HealthIncident.IncidentStatus.RESOLVED, result.getStatus());
        verify(healthIncidentRepository).save(existing);
    }

    @Test
    void updateHealthIncident_shouldThrowWhenStudentMissing() {
        HealthIncident existing = new HealthIncident();
        HealthIncident updatePayload = new HealthIncident();
        Student student = new Student();
        student.setId(11L);
        updatePayload.setStudent(student);

        when(healthIncidentRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(studentRepository.findById(11L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> healthIncidentService.updateHealthIncident(5L, updatePayload));
    }

    @Test
    void updateHealthIncident_shouldThrowWhenUserMissing() {
        HealthIncident existing = new HealthIncident();
        HealthIncident updatePayload = new HealthIncident();
        User user = new User();
        user.setId(22L);
        updatePayload.setReportedBy(user);

        when(healthIncidentRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(userRepository.findById(22L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> healthIncidentService.updateHealthIncident(5L, updatePayload));
    }

    @Test
    void deleteHealthIncident_shouldDelete() {
        when(healthIncidentRepository.existsById(1L)).thenReturn(true);
        healthIncidentService.deleteHealthIncident(1L);
        verify(healthIncidentRepository).deleteById(1L);
    }

    @Test
    void getHealthIncidentsByStudentId_shouldReturnList() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        when(healthIncidentRepository.findByStudent_Id(1L)).thenReturn(List.of(new HealthIncident()));
        assertFalse(healthIncidentService.getHealthIncidentsByStudentId(1L).isEmpty());
    }

    @Test
    void getHealthIncidentsByReportedById_shouldReturnList() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(healthIncidentRepository.findByReportedBy_Id(1L)).thenReturn(List.of(new HealthIncident()));
        assertFalse(healthIncidentService.getHealthIncidentsByReportedById(1L).isEmpty());
    }

    @Test
    void getHealthIncidentsByIncidentTime_shouldReturnList() {
        LocalDateTime time = LocalDateTime.now();
        when(healthIncidentRepository.findByIncidentTime(time)).thenReturn(List.of(new HealthIncident()));
        assertFalse(healthIncidentService.getHealthIncidentsByIncidentTime(time).isEmpty());
    }

    @Test
    void getHealthIncidentsByStatus_shouldDelegateToRepository() {
        List<HealthIncident> expected = List.of(new HealthIncident());
        when(healthIncidentRepository.findByStatus(HealthIncident.IncidentStatus.REPORTED)).thenReturn(expected);
        List<HealthIncident> result = healthIncidentService.getHealthIncidentsByStatus(HealthIncident.IncidentStatus.REPORTED);
        assertSame(expected, result);
    }

    @Test
    void getHealthIncidentsByIncidentType_shouldReturnList() {
        when(healthIncidentRepository.findByIncidentType(any())).thenReturn(List.of(new HealthIncident()));
        assertFalse(healthIncidentService.getHealthIncidentsByIncidentType(null).isEmpty());
    }

    @Test
    void getHealthIncidentsByIncidentTimeAndStatus_shouldReturnList() {
        when(healthIncidentRepository.findByIncidentTimeAndStatus(any(), any())).thenReturn(List.of(new HealthIncident()));
        assertFalse(healthIncidentService.getHealthIncidentsByIncidentTimeAndStatus(LocalDateTime.now(), HealthIncident.IncidentStatus.REPORTED).isEmpty());
    }

    @Test
    void getHealthIncidentsByStudentIdAndIncidentTime_shouldReturnList() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        when(healthIncidentRepository.findByStudent_IdAndIncidentTime(any(), any())).thenReturn(List.of(new HealthIncident()));
        assertFalse(healthIncidentService.getHealthIncidentsByStudentIdAndIncidentTime(1L, LocalDateTime.now()).isEmpty());
    }
}
