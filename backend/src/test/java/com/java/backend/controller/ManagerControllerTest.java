package com.java.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backend.entity.Manager;
import com.java.backend.enums.Role;
import com.java.backend.exception.ManagerNotFoundException;
import com.java.backend.service.ManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ManagerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ManagerService managerService;

    @InjectMocks
    private ManagerController managerController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Manager manager;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(managerController).build();
        manager = new Manager();
        manager.setId(1L);
        manager.setFullName("Test Manager");
        manager.setRole(Role.MANAGER);
        manager.setPhoneNumber("0123456789");
    }

    @Test
    void getAllManagers_Success() throws Exception {
        when(managerService.getAllManagers()).thenReturn(Arrays.asList(manager));
        mockMvc.perform(get("/api/managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getManagerById_Success() throws Exception {
        when(managerService.getManagerById(1L)).thenReturn(manager);
        mockMvc.perform(get("/api/managers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getManagerById_NotFound() throws Exception {
        when(managerService.getManagerById(1L)).thenThrow(new ManagerNotFoundException(1L));
        mockMvc.perform(get("/api/managers/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createManager_Success() throws Exception {
        when(managerService.saveManager(any())).thenReturn(manager);
        mockMvc.perform(post("/api/managers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manager)))
                .andExpect(status().isOk());
    }

    @Test
    void createManager_NullRole() throws Exception {
        manager.setRole(null);
        mockMvc.perform(post("/api/managers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manager)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Trường role là bắt buộc và phải là MANAGER"));
    }

    @Test
    void createManager_InvalidRole() throws Exception {
        manager.setRole(Role.ADMIN);
        mockMvc.perform(post("/api/managers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manager)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Khi tạo quản lý, role chỉ được phép là MANAGER"));
    }

    @Test
    void createManager_NullPhoneNumber() throws Exception {
        manager.setPhoneNumber(null);
        mockMvc.perform(post("/api/managers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manager)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Số điện thoại không được để trống"));
    }

    @Test
    void createManager_EmptyPhoneNumber() throws Exception {
        manager.setPhoneNumber("   ");
        mockMvc.perform(post("/api/managers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manager)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Số điện thoại không được để trống"));
    }

    @Test
    void createManager_InvalidPhoneNumberFormat() throws Exception {
        manager.setPhoneNumber("123456789"); // not starting with 0 or not 10 digits
        mockMvc.perform(post("/api/managers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manager)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Số điện thoại phải gồm đúng 10 chữ số và bắt đầu bằng 0"));
    }

    @Test
    void updateManager_Success() throws Exception {
        when(managerService.updateManager(any())).thenReturn(manager);
        mockMvc.perform(put("/api/managers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manager)))
                .andExpect(status().isOk());
    }

    @Test
    void updateManager_EmptyPhoneNumber() throws Exception {
        manager.setPhoneNumber("   ");
        mockMvc.perform(put("/api/managers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manager)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Số điện thoại không được để trống"));
    }

    @Test
    void updateManager_InvalidPhoneNumberFormat() throws Exception {
        manager.setPhoneNumber("0123");
        mockMvc.perform(put("/api/managers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manager)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Số điện thoại phải gồm đúng 10 chữ số và bắt đầu bằng 0"));
    }

    @Test
    void updateManager_NoPhoneNumber() throws Exception {
        manager.setPhoneNumber(null); // Valid path, skipped phone update
        when(managerService.updateManager(any())).thenReturn(manager);
        mockMvc.perform(put("/api/managers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manager)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteManager_Success() throws Exception {
        doNothing().when(managerService).deleteManager(1L);
        mockMvc.perform(delete("/api/managers/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteManager_NotFound() throws Exception {
        doThrow(new ManagerNotFoundException(1L)).when(managerService).deleteManager(1L);
        mockMvc.perform(delete("/api/managers/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}
