package com.java.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.backend.entity.MedicalSupply;
import com.java.backend.service.MedicalSupplyService;

@RestController
@RequestMapping("/api/medical-supplies")
@CrossOrigin(origins = "http://localhost:3000")
public class MedicalSupplyController {

    @Autowired
    private MedicalSupplyService medicalSupplyService;

    // Tạo mới vật tư y tế
    @PostMapping
    public ResponseEntity<?> createMedicalSupply(@RequestBody MedicalSupply medicalSupply) {
        try {
            MedicalSupply savedSupply = medicalSupplyService.createMedicalSupply(medicalSupply);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSupply);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Lỗi server: " + e.getMessage());
        }
    }

    // Lấy tất cả vật tư y tế
    @GetMapping
    public ResponseEntity<List<MedicalSupply>> getAllMedicalSupplies() {
        try {
            List<MedicalSupply> supplies = medicalSupplyService.getAllMedicalSupplies();
            return ResponseEntity.ok(supplies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Lấy vật tư y tế theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicalSupplyById(@PathVariable Long id) {
        try {
            Optional<MedicalSupply> supply = medicalSupplyService.getMedicalSupplyById(id);
            if (supply.isPresent()) {
                return ResponseEntity.ok(supply.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Lỗi server: " + e.getMessage());
        }
    }

    // Cập nhật vật tư y tế
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedicalSupply(@PathVariable Long id, @RequestBody MedicalSupply medicalSupply) {
        try {
            MedicalSupply updatedSupply = medicalSupplyService.updateMedicalSupply(id, medicalSupply);
            return ResponseEntity.ok(updatedSupply);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Lỗi server: " + e.getMessage());
        }
    }

    // Xóa vật tư y tế
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedicalSupply(@PathVariable Long id) {
        try {
            medicalSupplyService.deleteMedicalSupply(id);
            return ResponseEntity.ok("Vật tư y tế đã được xóa thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Lỗi server: " + e.getMessage());
        }
    }

    // Tìm kiếm vật tư y tế theo tên
    @GetMapping("/search")
    public ResponseEntity<List<MedicalSupply>> searchMedicalSupplies(@RequestBody String name) {
        try {
            List<MedicalSupply> supplies = medicalSupplyService.searchMedicalSupplies(name);
            return ResponseEntity.ok(supplies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Lấy vật tư y tế theo danh mục
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MedicalSupply>> getMedicalSuppliesByCategory(@PathVariable String category) {
        try {
            List<MedicalSupply> supplies = medicalSupplyService.getMedicalSuppliesByCategory(category);
            return ResponseEntity.ok(supplies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Lấy vật tư y tế có số lượng thấp
    @GetMapping("/low-stock/{threshold}")
    public ResponseEntity<List<MedicalSupply>> getLowStockSupplies(@PathVariable Integer threshold) {
        try {
            List<MedicalSupply> supplies = medicalSupplyService.getLowStockSupplies(threshold);
            return ResponseEntity.ok(supplies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
