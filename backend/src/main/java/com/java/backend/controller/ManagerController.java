package com.java.backend.controller;

import com.java.backend.entity.Manager;
import com.java.backend.enums.Role;
import com.java.backend.exception.ManagerNotFoundException;
import com.java.backend.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/managers")
@CrossOrigin(origins = "http://localhost:3000")
public class ManagerController {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");
    private static final String PHONE_VALIDATION_MESSAGE =
            "Số điện thoại phải gồm đúng 10 chữ số và bắt đầu bằng 0";

    @Autowired
    private ManagerService managerService;

    @PostMapping
    public ResponseEntity<Object> createManager(@RequestBody Manager manager) {
        if (manager.getRole() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Trường role là bắt buộc và phải là MANAGER"));
        }
        if (manager.getRole() != Role.MANAGER) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Khi tạo quản lý, role chỉ được phép là MANAGER"));
        }
        if (manager.getPhoneNumber() == null || manager.getPhoneNumber().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Số điện thoại không được để trống"));
        }
        String phoneNumber = manager.getPhoneNumber().trim();
        if (!isValidPhoneNumber(phoneNumber)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", PHONE_VALIDATION_MESSAGE));
        }
        manager.setPhoneNumber(phoneNumber);

        Manager saved = managerService.saveManager(manager);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Manager> getAllManagers() {
        return managerService.getAllManagers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getManager(@PathVariable Long id) {
        try {
            Manager manager = managerService.getManagerById(id);
            return ResponseEntity.ok(manager);
        } catch (ManagerNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateManager(@PathVariable Long id, @RequestBody Manager manager) {
        if (manager.getPhoneNumber() != null) {
            String phoneNumber = manager.getPhoneNumber().trim();
            if (phoneNumber.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Số điện thoại không được để trống"));
            }
            if (!isValidPhoneNumber(phoneNumber)) {
                return ResponseEntity.badRequest().body(Map.of("message", PHONE_VALIDATION_MESSAGE));
            }
            manager.setPhoneNumber(phoneNumber);
        }
        manager.setId(id);
        Manager updated = managerService.updateManager(manager);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteManager(@PathVariable Long id) {
        try {
            managerService.deleteManager(id);
            return ResponseEntity.ok().build();
        } catch (ManagerNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches();
    }
}
