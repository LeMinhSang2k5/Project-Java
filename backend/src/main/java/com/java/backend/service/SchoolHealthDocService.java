package com.java.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.java.backend.entity.SchoolHealthDoc;
import com.java.backend.repository.SchoolHealthDocRepository;

@Service
public class SchoolHealthDocService {

    private final SchoolHealthDocRepository schoolHealthDocRepository;

    public SchoolHealthDocService(SchoolHealthDocRepository schoolHealthDocRepository) {
        this.schoolHealthDocRepository = schoolHealthDocRepository;
    }

    public List<SchoolHealthDoc> getAllSchoolHealthDocs() {
        return schoolHealthDocRepository.findAll();
    }

    public SchoolHealthDoc getSchoolHealthDocById(Long id) {
        return schoolHealthDocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School health doc not found with id: " + id));
    }

    public SchoolHealthDoc createSchoolHealthDoc(SchoolHealthDoc schoolHealthDoc) {
        return schoolHealthDocRepository.save(schoolHealthDoc);
    }


    public SchoolHealthDoc updateSchoolHealthDoc(Long id, SchoolHealthDoc schoolHealthDoc) {
        SchoolHealthDoc existingSchoolHealthDoc = getSchoolHealthDocById(id);
        existingSchoolHealthDoc.setTitle(schoolHealthDoc.getTitle());
        existingSchoolHealthDoc.setContent(schoolHealthDoc.getContent());
        existingSchoolHealthDoc.setUrl(schoolHealthDoc.getUrl());
        return schoolHealthDocRepository.save(existingSchoolHealthDoc);
    }

    public void deleteSchoolHealthDoc(Long id) {
        SchoolHealthDoc schoolHealthDoc = getSchoolHealthDocById(id);
        schoolHealthDocRepository.delete(schoolHealthDoc);
    }
}
