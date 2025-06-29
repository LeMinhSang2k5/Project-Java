package com.java.backend.controller;

import com.java.backend.entity.Medical;
import com.java.backend.service.MedicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/medical")
@CrossOrigin(origins = "http://localhost:3000")
public class MedicalController {

    @Autowired
    private MedicalService medicalService;

    // Tạo yêu cầu thuốc mới
    @PostMapping
    public ResponseEntity<?> createMedicalRequest(@RequestBody Map<String, Object> request) {
        try {
            Long studentId = Long.valueOf(request.get("studentId").toString());
            Long parentId = Long.valueOf(request.get("parentId").toString());
            String medicalName = (String) request.get("medicalName");
            String dosage = (String) request.get("dosage");
            String note = (String) request.get("note");

            Medical medical = medicalService.createMedicalRequest(studentId, parentId, medicalName, dosage, note);
            return ResponseEntity.ok(medical);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi tạo yêu cầu thuốc: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Lấy tất cả yêu cầu thuốc
    @GetMapping
    public ResponseEntity<List<Medical>> getAllMedicalRequests() {
        List<Medical> medicals = medicalService.getAllMedicalRequests();
        return ResponseEntity.ok(medicals);
    }

    // Lấy yêu cầu thuốc theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Medical> getMedicalRequestById(@PathVariable Long id) {
        try {
            Medical medical = medicalService.getMedicalRequestById(id);
            return ResponseEntity.ok(medical);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Lấy yêu cầu thuốc theo student ID
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Medical>> getMedicalRequestsByStudentId(@PathVariable Long studentId) {
        List<Medical> medicals = medicalService.getMedicalRequestsByStudentId(studentId);
        return ResponseEntity.ok(medicals);
    }

    // Lấy yêu cầu thuốc theo parent ID
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<Medical>> getMedicalRequestsByParentId(@PathVariable Long parentId) {
        List<Medical> medicals = medicalService.getMedicalRequestsByParentId(parentId);
        return ResponseEntity.ok(medicals);
    }

    // Lấy yêu cầu thuốc theo status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Medical>> getMedicalRequestsByStatus(@PathVariable String status) {
        List<Medical> medicals = medicalService.getMedicalRequestsByStatus(status);
        return ResponseEntity.ok(medicals);
    }

    // Cập nhật trạng thái yêu cầu thuốc
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateMedicalRequestStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            String approvedBy = request.get("approvedBy");
            
            Medical medical = medicalService.updateMedicalRequestStatus(id, status, approvedBy);
            return ResponseEntity.ok(medical);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi cập nhật trạng thái: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Cập nhật yêu cầu thuốc
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedicalRequest(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String medicalName = request.get("medicalName");
            String dosage = request.get("dosage");
            String note = request.get("note");
            
            Medical medical = medicalService.updateMedicalRequest(id, medicalName, dosage, note);
            return ResponseEntity.ok(medical);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi cập nhật yêu cầu thuốc: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Xóa yêu cầu thuốc
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedicalRequest(@PathVariable Long id) {
        try {
            medicalService.deleteMedicalRequest(id);
            Map<String, String> success = new HashMap<>();
            success.put("message", "Xóa yêu cầu thuốc thành công");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi xóa yêu cầu thuốc: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
} 