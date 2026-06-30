package com.java.backend.service;

import com.java.backend.entity.SchoolHealthDoc;
import com.java.backend.repository.SchoolHealthDocRepository;
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
class SchoolHealthDocServiceTest {

    @Mock
    private SchoolHealthDocRepository repository;

    @InjectMocks
    private SchoolHealthDocService service;

    private SchoolHealthDoc doc;

    @BeforeEach
    void setUp() {
        doc = new SchoolHealthDoc();
        doc.setId(1L);
        doc.setTitle("Covid Rules");
        doc.setContent("Wash hands");
        doc.setUrl("link.com");
    }

    @Test
    void getAllSchoolHealthDocs_Success() {
        when(repository.findAll()).thenReturn(Arrays.asList(doc));
        List<SchoolHealthDoc> list = service.getAllSchoolHealthDocs();
        assertEquals(1, list.size());
    }

    @Test
    void getSchoolHealthDocById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(doc));
        SchoolHealthDoc found = service.getSchoolHealthDocById(1L);
        assertNotNull(found);
    }

    @Test
    void getSchoolHealthDocById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getSchoolHealthDocById(1L));
    }

    @Test
    void createSchoolHealthDoc_Success() {
        when(repository.save(any(SchoolHealthDoc.class))).thenReturn(doc);
        SchoolHealthDoc saved = service.createSchoolHealthDoc(doc);
        assertNotNull(saved);
    }

    @Test
    void updateSchoolHealthDoc_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(doc));
        when(repository.save(any(SchoolHealthDoc.class))).thenReturn(doc);
        
        SchoolHealthDoc updatedDoc = new SchoolHealthDoc();
        updatedDoc.setTitle("New Title");
        updatedDoc.setContent("New Content");
        updatedDoc.setUrl("newlink.com");
        
        SchoolHealthDoc updated = service.updateSchoolHealthDoc(1L, updatedDoc);
        
        assertNotNull(updated);
        assertEquals("New Title", doc.getTitle());
        assertEquals("New Content", doc.getContent());
        assertEquals("newlink.com", doc.getUrl());
    }

    @Test
    void deleteSchoolHealthDoc_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(doc));
        doNothing().when(repository).delete(any(SchoolHealthDoc.class));
        assertDoesNotThrow(() -> service.deleteSchoolHealthDoc(1L));
        verify(repository, times(1)).delete(doc);
    }
}
