package com.java.backend.controller;

import com.java.backend.entity.Nurse;
import com.java.backend.enums.Role;
import com.java.backend.exception.NurseNotFoundException;
import com.java.backend.service.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/nurses")
@CrossOrigin(origins = "http://localhost:3000")
public class NurseController {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");
    private static final String PHONE_VALIDATION_MESSAGE =
            "Số điện thoại phải gồm đúng 10 chữ số và bắt đầu bằng 0";

    @Autowired
    private NurseService nurseService;

    @PostMapping
    public ResponseEntity<?> createNurse(@RequestBody Nurse nurse) {
        try {
            if (nurse.getRole() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Trường role là bắt buộc và phải là SCHOOL_NURSE"));
            }
            if (nurse.getRole() != Role.SCHOOL_NURSE) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Khi tạo nhân viên y tế, role chỉ được phép là SCHOOL_NURSE"));
            }
            if (nurse.getPhoneNumber() == null || nurse.getPhoneNumber().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Số điện thoại không được để trống"));
            }
            String phoneNumber = nurse.getPhoneNumber().trim();
            if (!isValidPhoneNumber(phoneNumber)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", PHONE_VALIDATION_MESSAGE));
            }
            nurse.setPhoneNumber(phoneNumber);

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
        } catch (NurseNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Error getting nurse: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error getting nurse: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNurse(@PathVariable Long id, @RequestBody Nurse nurse) {
        try {
            if (nurse.getPhoneNumber() != null) {
                String phoneNumber = nurse.getPhoneNumber().trim();
                if (phoneNumber.isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Số điện thoại không được để trống"));
                }
                if (!isValidPhoneNumber(phoneNumber)) {
                    return ResponseEntity.badRequest().body(Map.of("message", PHONE_VALIDATION_MESSAGE));
                }
                nurse.setPhoneNumber(phoneNumber);
            }
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
        } catch (NurseNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Error deleting nurse: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error deleting nurse: " + e.getMessage());
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches();
    }
}