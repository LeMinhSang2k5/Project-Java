package com.java.backend.service;

import com.java.backend.entity.Nurse;
import com.java.backend.enums.Role;
import com.java.backend.exception.NurseNotFoundException;
import com.java.backend.repository.NurseRepository;
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
class NurseServiceTest {

    @Mock
    private NurseRepository nurseRepository;

    @InjectMocks
    private NurseService nurseService;

    private Nurse nurse;

    @BeforeEach
    void setUp() {
        nurse = new Nurse();
        nurse.setId(1L);
        nurse.setEmail("nurse@gmail.com");
    }

    @Test
    void saveNurse_Success_InactiveToActive() {
        nurse.setActive(false);
        when(nurseRepository.save(any(Nurse.class))).thenReturn(nurse);
        Nurse saved = nurseService.saveNurse(nurse);
        assertNotNull(saved);
        assertTrue(nurse.isActive());
        assertEquals(Role.SCHOOL_NURSE, nurse.getRole());
    }

    @Test
    void saveNurse_Success_AlreadyActive() {
        nurse.setActive(true);
        when(nurseRepository.save(any(Nurse.class))).thenReturn(nurse);
        Nurse saved = nurseService.saveNurse(nurse);
        assertNotNull(saved);
        assertTrue(nurse.isActive());
    }

    @Test
    void saveNurse_Exception() {
        when(nurseRepository.save(any(Nurse.class))).thenThrow(new RuntimeException("DB Error"));
        assertThrows(RuntimeException.class, () -> nurseService.saveNurse(nurse));
    }

    @Test
    void getAllNurses_Success() {
        when(nurseRepository.findAll()).thenReturn(Arrays.asList(nurse));
        List<Nurse> list = nurseService.getAllNurses();
        assertEquals(1, list.size());
    }

    @Test
    void getAllNurses_Exception() {
        when(nurseRepository.findAll()).thenThrow(new RuntimeException("DB Error"));
        assertThrows(RuntimeException.class, () -> nurseService.getAllNurses());
    }

    @Test
    void getNurseById_Success() {
        when(nurseRepository.findById(1L)).thenReturn(Optional.of(nurse));
        Nurse found = nurseService.getNurseById(1L);
        assertNotNull(found);
    }

    @Test
    void getNurseById_NotFound() {
        when(nurseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NurseNotFoundException.class, () -> nurseService.getNurseById(1L));
    }

    @Test
    void updateNurse_Success() {
        when(nurseRepository.save(any(Nurse.class))).thenReturn(nurse);
        Nurse updated = nurseService.updateNurse(nurse);
        assertNotNull(updated);
    }

    @Test
    void deleteNurse_Success() {
        when(nurseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(nurseRepository).deleteById(1L);
        assertDoesNotThrow(() -> nurseService.deleteNurse(1L));
    }

    @Test
    void deleteNurse_NotFound() {
        when(nurseRepository.existsById(1L)).thenReturn(false);
        assertThrows(NurseNotFoundException.class, () -> nurseService.deleteNurse(1L));
    }
}
