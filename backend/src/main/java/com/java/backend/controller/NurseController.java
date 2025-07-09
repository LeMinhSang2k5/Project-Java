package com.java.backend.controller;

import com.java.backend.entity.Nurse;
import com.java.backend.service.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nurses")
@CrossOrigin(origins = "http://localhost:3000")
public class NurseController {

    @Autowired
    private NurseService nurseService;

    @PostMapping
    public ResponseEntity<?> createNurse(@RequestBody Nurse nurse) {
        try {
            System.out.println("Received nurse data: " + nurse.getEmail() + ", " + nurse.getFullName());
            Nurse saved = nurseService.saveNurse(nurse);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            System.err.println("Error creating nurse: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creating nurse: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllNurses() {
        try {
            List<Nurse> nurses = nurseService.getAllNurses();
            return ResponseEntity.ok(nurses);
        } catch (Exception e) {
            System.err.println("Error getting nurses: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error getting nurses: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNurse(@PathVariable Long id) {
        try {
            Nurse nurse = nurseService.getNurseById(id);
            return ResponseEntity.ok(nurse);
        } catch (Exception e) {
            System.err.println("Error getting nurse: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error getting nurse: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNurse(@PathVariable Long id, @RequestBody Nurse nurse) {
        try {
            nurse.setId(id);
            Nurse updated = nurseService.updateNurse(nurse);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            System.err.println("Error updating nurse: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error updating nurse: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNurse(@PathVariable Long id) {
        try {
            nurseService.deleteNurse(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error deleting nurse: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error deleting nurse: " + e.getMessage());
        }
    }
}