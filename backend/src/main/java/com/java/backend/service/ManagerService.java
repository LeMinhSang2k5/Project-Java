package com.java.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.backend.entity.Manager;
import com.java.backend.repository.ManagerRepository;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    public Manager saveManager(Manager manager) {
        return managerRepository.save(manager);
    }

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public Manager getManagerById(Long id) {
        return managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found with id: " + id));
    }

    public Manager updateManager(Manager manager) {
        return managerRepository.save(manager);
    }

    public void deleteManager(Long id) {
        managerRepository.deleteById(id);
    }
}