package com.java.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.java.backend.entity.MedicalSupply;
import com.java.backend.service.MedicalSupplyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MedicalSupplyControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private MedicalSupplyService medicalSupplyService;

    @InjectMocks
    private MedicalSupplyController medicalSupplyController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(medicalSupplyController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createMedicalSupply_shouldReturnCreatedWhenValid() throws Exception {
        MedicalSupply supply = sampleSupply();
        when(medicalSupplyService.createMedicalSupply(any(MedicalSupply.class))).thenReturn(supply);

        mockMvc.perform(post("/api/medical-supplies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(supply)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Paracetamol"));
    }

    @Test
    void createMedicalSupply_shouldReturnBadRequestWhenRuntimeException() throws Exception {
        when(medicalSupplyService.createMedicalSupply(any())).thenThrow(new RuntimeException("invalid name"));

        mockMvc.perform(post("/api/medical-supplies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleSupply())))
                .andExpect(status().isBadRequest());
    }



    @Test
    void getAllMedicalSupplies_shouldReturnOk() throws Exception {
        when(medicalSupplyService.getAllMedicalSupplies()).thenReturn(List.of(sampleSupply()));
        mockMvc.perform(get("/api/medical-supplies"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllMedicalSupplies_shouldReturnInternalServerError() throws Exception {
        when(medicalSupplyService.getAllMedicalSupplies()).thenThrow(new RuntimeException());
        mockMvc.perform(get("/api/medical-supplies"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void searchMedicalSupplies_shouldReturnOk() throws Exception {
        when(medicalSupplyService.searchMedicalSupplies("Para")).thenReturn(List.of(sampleSupply()));
        mockMvc.perform(get("/api/medical-supplies/search").param("name", "Para"))
                .andExpect(status().isOk());
    }

    @Test
    void searchMedicalSupplies_shouldReturnBadRequestIfEmpty() throws Exception {
        mockMvc.perform(get("/api/medical-supplies/search").param("name", " "))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchMedicalSupplies_shouldReturnInternalServerError() throws Exception {
        when(medicalSupplyService.searchMedicalSupplies("Para")).thenThrow(new RuntimeException());
        mockMvc.perform(get("/api/medical-supplies/search").param("name", "Para"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getMedicalSuppliesByCategory_shouldReturnOk() throws Exception {
        when(medicalSupplyService.getMedicalSuppliesByCategory("Thuốc")).thenReturn(List.of(sampleSupply()));
        mockMvc.perform(get("/api/medical-supplies/category/Thuốc"))
                .andExpect(status().isOk());
    }

    @Test
    void getMedicalSuppliesByCategory_shouldReturnInternalServerError() throws Exception {
        when(medicalSupplyService.getMedicalSuppliesByCategory("Thuốc")).thenThrow(new RuntimeException());
        mockMvc.perform(get("/api/medical-supplies/category/Thuốc"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getLowStockSupplies_shouldReturnOk() throws Exception {
        when(medicalSupplyService.getLowStockSupplies(10)).thenReturn(List.of(sampleSupply()));
        mockMvc.perform(get("/api/medical-supplies/low-stock/10"))
                .andExpect(status().isOk());
    }

    @Test
    void getLowStockSupplies_shouldReturnInternalServerError() throws Exception {
        when(medicalSupplyService.getLowStockSupplies(10)).thenThrow(new RuntimeException());
        mockMvc.perform(get("/api/medical-supplies/low-stock/10"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getMedicalSupplyById_shouldReturnOk() throws Exception {
        when(medicalSupplyService.getMedicalSupplyById(1L)).thenReturn(Optional.of(sampleSupply()));
        mockMvc.perform(get("/api/medical-supplies/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getMedicalSupplyById_shouldReturnNotFoundWhenMissing() throws Exception {
        when(medicalSupplyService.getMedicalSupplyById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/medical-supplies/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMedicalSupplyById_shouldReturnInternalServerError() throws Exception {
        when(medicalSupplyService.getMedicalSupplyById(1L)).thenThrow(new RuntimeException());
        mockMvc.perform(get("/api/medical-supplies/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateMedicalSupply_shouldReturnOk() throws Exception {
        when(medicalSupplyService.updateMedicalSupply(eq(1L), any())).thenReturn(sampleSupply());
        mockMvc.perform(put("/api/medical-supplies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleSupply())))
                .andExpect(status().isOk());
    }

    @Test
    void updateMedicalSupply_shouldReturnBadRequest() throws Exception {
        when(medicalSupplyService.updateMedicalSupply(eq(1L), any())).thenThrow(new RuntimeException("Bad Request"));
        mockMvc.perform(put("/api/medical-supplies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleSupply())))
                .andExpect(status().isBadRequest());
    }



    @Test
    void deleteMedicalSupply_shouldReturnOk() throws Exception {
        doNothing().when(medicalSupplyService).deleteMedicalSupply(1L);
        mockMvc.perform(delete("/api/medical-supplies/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMedicalSupply_shouldReturnBadRequest() throws Exception {
        doThrow(new RuntimeException("Bad Request")).when(medicalSupplyService).deleteMedicalSupply(1L);
        mockMvc.perform(delete("/api/medical-supplies/1"))
                .andExpect(status().isBadRequest());
    }



    private MedicalSupply sampleSupply() {
        MedicalSupply supply = new MedicalSupply();
        supply.setId(1L);
        supply.setName("Paracetamol");
        supply.setQuantity(20);
        supply.setCategory("Thuốc");
        supply.setExpiryDate(LocalDate.of(2026, 12, 31));
        return supply;
    }
}
