package com.java.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backend.entity.SchoolHealthDoc;
import com.java.backend.service.SchoolHealthDocService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SchoolHealthDocControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SchoolHealthDocService schoolHealthDocService;

    @InjectMocks
    private SchoolHealthDocController schoolHealthDocController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private SchoolHealthDoc doc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(schoolHealthDocController).build();
        doc = new SchoolHealthDoc();
        doc.setId(1L);
        doc.setTitle("Test Doc");
        doc.setContent("Test Content");
        doc.setUrl("http://example.com");
    }

    @Test
    void getAllSchoolHealthDocs_Success() throws Exception {
        when(schoolHealthDocService.getAllSchoolHealthDocs()).thenReturn(Arrays.asList(doc));
        mockMvc.perform(get("/api/school-health-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getSchoolHealthDocById_Success() throws Exception {
        when(schoolHealthDocService.getSchoolHealthDocById(1L)).thenReturn(doc);
        mockMvc.perform(get("/api/school-health-docs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createSchoolHealthDoc_Success() throws Exception {
        when(schoolHealthDocService.createSchoolHealthDoc(any(SchoolHealthDoc.class))).thenReturn(doc);
        mockMvc.perform(post("/api/school-health-docs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doc)))
                .andExpect(status().isOk());
    }

    @Test
    void updateSchoolHealthDoc_Success() throws Exception {
        when(schoolHealthDocService.updateSchoolHealthDoc(eq(1L), any(SchoolHealthDoc.class))).thenReturn(doc);
        mockMvc.perform(put("/api/school-health-docs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doc)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteSchoolHealthDoc_Success() throws Exception {
        doNothing().when(schoolHealthDocService).deleteSchoolHealthDoc(1L);
        mockMvc.perform(delete("/api/school-health-docs/1"))
                .andExpect(status().isOk());
    }
}
