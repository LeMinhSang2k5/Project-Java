package com.java.backend.controller;

import com.java.backend.entity.Manager;
import com.java.backend.enums.Role;
import com.java.backend.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/managers")
@CrossOrigin(origins = "http://localhost:3000")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @PostMapping
    public ResponseEntity<?> createManager(@RequestBody Manager manager) {
        if (manager.getRole() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Trường role là bắt buộc và phải là MANAGER"));
        }
        if (manager.getRole() != Role.MANAGER) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Khi tạo quản lý, role chỉ được phép là MANAGER"));
        }

        Manager saved = managerService.saveManager(manager);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Manager> getAllManagers() {
        return managerService.getAllManagers();
    }

    @GetMapping("/{id}")
    public Manager getManager(@PathVariable Long id) {
        return managerService.getManagerById(id);
    }

    @PutMapping("/{id}")
    public Manager updateManager(@PathVariable Long id, @RequestBody Manager manager) {
        manager.setId(id);
        return managerService.updateManager(manager);
    }

    @DeleteMapping("/{id}")
    public void deleteManager(@PathVariable Long id) {
        managerService.deleteManager(id);
    }
}