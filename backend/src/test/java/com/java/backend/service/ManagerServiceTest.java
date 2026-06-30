package com.java.backend.service;

import com.java.backend.entity.Manager;
import com.java.backend.enums.Role;
import com.java.backend.exception.ManagerNotFoundException;
import com.java.backend.repository.ManagerRepository;
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
class ManagerServiceTest {

    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private ManagerService managerService;

    private Manager manager;

    @BeforeEach
    void setUp() {
        manager = new Manager();
        manager.setId(1L);
        manager.setFullName("Test Manager");
    }

    @Test
    void saveManager_Success() {
        when(managerRepository.save(any(Manager.class))).thenReturn(manager);
        Manager saved = managerService.saveManager(manager);
        assertNotNull(saved);
        assertEquals(Role.MANAGER, manager.getRole());
    }

    @Test
    void getAllManagers_Success() {
        when(managerRepository.findAll()).thenReturn(Arrays.asList(manager));
        List<Manager> list = managerService.getAllManagers();
        assertEquals(1, list.size());
    }

    @Test
    void getManagerById_Success() {
        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
        Manager found = managerService.getManagerById(1L);
        assertNotNull(found);
    }

    @Test
    void getManagerById_NotFound() {
        when(managerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ManagerNotFoundException.class, () -> managerService.getManagerById(1L));
    }

    @Test
    void updateManager_Success() {
        when(managerRepository.save(any(Manager.class))).thenReturn(manager);
        Manager updated = managerService.updateManager(manager);
        assertNotNull(updated);
    }

    @Test
    void deleteManager_Success() {
        when(managerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(managerRepository).deleteById(1L);
        assertDoesNotThrow(() -> managerService.deleteManager(1L));
        verify(managerRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteManager_NotFound() {
        when(managerRepository.existsById(1L)).thenReturn(false);
        assertThrows(ManagerNotFoundException.class, () -> managerService.deleteManager(1L));
    }
}
