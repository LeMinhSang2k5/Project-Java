package com.java.backend.controller;

import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.backend.entity.MedicalSupply;
import com.java.backend.service.MedicalSupplyService;

@RestController
@RequestMapping("/api/medical-supplies")
@CrossOrigin(origins = "http://localhost:3000")
public class MedicalSupplyController {

    @Autowired
    private MedicalSupplyService medicalSupplyService;

    @PostMapping
    public ResponseEntity<?> createMedicalSupply(@RequestBody MedicalSupply medicalSupply) {
        try {
            MedicalSupply savedSupply = medicalSupplyService.createMedicalSupply(medicalSupply);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSupply);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi server: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<MedicalSupply>> getAllMedicalSupplies() {
        try {
            List<MedicalSupply> supplies = medicalSupplyService.getAllMedicalSupplies();
            return ResponseEntity.ok(supplies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMedicalSupplies(@RequestParam String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Tên tìm kiếm không được để trống"));
            }
            List<MedicalSupply> supplies = medicalSupplyService.searchMedicalSupplies(name.trim());
            return ResponseEntity.ok(supplies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<MedicalSupply>> getMedicalSuppliesByCategory(@PathVariable String category) {
        try {
            List<MedicalSupply> supplies = medicalSupplyService.getMedicalSuppliesByCategory(category);
            return ResponseEntity.ok(supplies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/low-stock/{threshold}")
    public ResponseEntity<List<MedicalSupply>> getLowStockSupplies(@PathVariable Integer threshold) {
        try {
            List<MedicalSupply> supplies = medicalSupplyService.getLowStockSupplies(threshold);
            return ResponseEntity.ok(supplies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicalSupplyById(@PathVariable Long id) {
        try {
            Optional<MedicalSupply> supply = medicalSupplyService.getMedicalSupplyById(id);
            if (supply.isPresent()) {
                return ResponseEntity.ok(supply.get());
            }
            return ResponseEntity.status(404)
                    .body(Map.of("message", "Không tìm thấy vật tư y tế với ID: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi server: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedicalSupply(@PathVariable Long id, @RequestBody MedicalSupply medicalSupply) {
        try {
            MedicalSupply updatedSupply = medicalSupplyService.updateMedicalSupply(id, medicalSupply);
            return ResponseEntity.ok(updatedSupply);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("không tồn tại")) {
                return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
            }
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi server: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedicalSupply(@PathVariable Long id) {
        try {
            medicalSupplyService.deleteMedicalSupply(id);
            return ResponseEntity.ok(Map.of("message", "Vật tư y tế đã được xóa thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi server: " + e.getMessage()));
        }
    }
}
