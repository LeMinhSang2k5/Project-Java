package com.java.backend.service;

import java.util.List;
import java.time.LocalDateTime;

import com.java.backend.entity.Medical;
import com.java.backend.entity.Student;
import com.java.backend.entity.Parent;
import com.java.backend.repository.MedicalRepository;
import com.java.backend.repository.StudentRepository;
import com.java.backend.repository.ParentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicalService {
    @Autowired
    private MedicalRepository medicalRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Transactional
    public Medical createMedicalRequest(Long studentId, Long parentId, String medicalName, String dosage, String note) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
        
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found with ID: " + parentId));

        Medical medical = new Medical(student, parent, medicalName, dosage, note);
        return medicalRepository.save(medical);
    }

    public List<Medical> getAllMedicalRequests() {
        return medicalRepository.findAll();
    }

    public Medical getMedicalRequestById(Long id) {
        return medicalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical request not found with id: " + id));
    }

    public List<Medical> getMedicalRequestsByStudentId(Long studentId) {
        return medicalRepository.findByStudentIdOrderByRequestDateDesc(studentId);
    }

    public List<Medical> getMedicalRequestsByParentId(Long parentId) {
        return medicalRepository.findByParentIdOrderByRequestDateDesc(parentId);
    }

    public List<Medical> getMedicalRequestsByStatus(String status) {
        return medicalRepository.findByStatusOrderByRequestDateDesc(status);
    }

    public List<Medical> getMedicalRequestsByStudentIdAndStatus(Long studentId, String status) {
        return medicalRepository.findByStudentIdAndStatusOrderByRequestDateDesc(studentId, status);
    }

    @Transactional
    public Medical updateMedicalRequestStatus(Long id, String status, String approvedBy) {
        Medical medical = getMedicalRequestById(id);
        medical.setStatus(status);
        
        if ("APPROVED".equals(status)) {
            medical.setApprovedBy(approvedBy);
            medical.setApprovedDate(LocalDateTime.now());
        } else if ("COMPLETED".equals(status)) {
            medical.setAdministeredBy(approvedBy);
            medical.setAdministeredDate(LocalDateTime.now());
        }
        
        return medicalRepository.save(medical);
    }

    @Transactional
    public Medical updateMedicalRequest(Long id, String medicalName, String dosage, String note) {
        Medical medical = getMedicalRequestById(id);
        medical.setMedicalName(medicalName);
        medical.setDosage(dosage);
        medical.setNote(note);
        return medicalRepository.save(medical);
    }

    public void deleteMedicalRequest(Long id) {
        if (!medicalRepository.existsById(id)) {
            throw new RuntimeException("Medical request not found with id: " + id);
        }
        medicalRepository.deleteById(id);
    }
}

