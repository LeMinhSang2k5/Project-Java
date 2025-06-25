package com.java.backend.controller;

import com.java.backend.entity.Manager;
import com.java.backend.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
@CrossOrigin(origins = "http://localhost:3000")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @PostMapping
    public ResponseEntity<Manager> createManager(@RequestBody Manager manager) {
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