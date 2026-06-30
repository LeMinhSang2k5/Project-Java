package com.java.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backend.entity.Nurse;
import com.java.backend.enums.Role;
import com.java.backend.exception.NurseNotFoundException;
import com.java.backend.service.NurseService;
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
class NurseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NurseService nurseService;

    @InjectMocks
    private NurseController nurseController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Nurse nurse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(nurseController).build();
        nurse = new Nurse();
        nurse.setId(1L);
        nurse.setRole(Role.SCHOOL_NURSE);
        nurse.setPhoneNumber("0123456789");
    }

    @Test
    void createNurse_Success() throws Exception {
        when(nurseService.saveNurse(any(Nurse.class))).thenReturn(nurse);
        
        mockMvc.perform(post("/api/nurses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isOk());
    }

    @Test
    void createNurse_NullRole() throws Exception {
        nurse.setRole(null);
        mockMvc.perform(post("/api/nurses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNurse_WrongRole() throws Exception {
        nurse.setRole(Role.ADMIN);
        mockMvc.perform(post("/api/nurses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNurse_NullPhone() throws Exception {
        nurse.setPhoneNumber(null);
        mockMvc.perform(post("/api/nurses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNurse_EmptyPhone() throws Exception {
        nurse.setPhoneNumber("");
        mockMvc.perform(post("/api/nurses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNurse_InvalidPhone() throws Exception {
        nurse.setPhoneNumber("123");
        mockMvc.perform(post("/api/nurses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNurse_Exception() throws Exception {
        when(nurseService.saveNurse(any(Nurse.class))).thenThrow(new RuntimeException("DB Error"));
        mockMvc.perform(post("/api/nurses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllNurses_Success() throws Exception {
        when(nurseService.getAllNurses()).thenReturn(Arrays.asList(nurse));
        mockMvc.perform(get("/api/nurses"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllNurses_Exception() throws Exception {
        when(nurseService.getAllNurses()).thenThrow(new RuntimeException("Error"));
        mockMvc.perform(get("/api/nurses"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getNurse_Success() throws Exception {
        when(nurseService.getNurseById(1L)).thenReturn(nurse);
        mockMvc.perform(get("/api/nurses/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getNurse_NotFound() throws Exception {
        when(nurseService.getNurseById(1L)).thenThrow(new NurseNotFoundException(1L));
        mockMvc.perform(get("/api/nurses/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getNurse_Exception() throws Exception {
        when(nurseService.getNurseById(1L)).thenThrow(new RuntimeException("Error"));
        mockMvc.perform(get("/api/nurses/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateNurse_Success() throws Exception {
        when(nurseService.updateNurse(any(Nurse.class))).thenReturn(nurse);
        mockMvc.perform(put("/api/nurses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isOk());
    }

    @Test
    void updateNurse_EmptyPhone() throws Exception {
        nurse.setPhoneNumber("   ");
        mockMvc.perform(put("/api/nurses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateNurse_InvalidPhone() throws Exception {
        nurse.setPhoneNumber("123");
        mockMvc.perform(put("/api/nurses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateNurse_NullPhone() throws Exception {
        nurse.setPhoneNumber(null); // Should skip update and succeed
        when(nurseService.updateNurse(any(Nurse.class))).thenReturn(nurse);
        mockMvc.perform(put("/api/nurses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isOk());
    }

    @Test
    void updateNurse_Exception() throws Exception {
        when(nurseService.updateNurse(any(Nurse.class))).thenThrow(new RuntimeException("Error"));
        mockMvc.perform(put("/api/nurses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nurse)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteNurse_Success() throws Exception {
        doNothing().when(nurseService).deleteNurse(1L);
        mockMvc.perform(delete("/api/nurses/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteNurse_NotFound() throws Exception {
        doThrow(new NurseNotFoundException(1L)).when(nurseService).deleteNurse(1L);
        mockMvc.perform(delete("/api/nurses/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNurse_Exception() throws Exception {
        doThrow(new RuntimeException("Error")).when(nurseService).deleteNurse(1L);
        mockMvc.perform(delete("/api/nurses/1"))
                .andExpect(status().isInternalServerError());
    }
}
