package com.java.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.backend.entity.Parent;
import com.java.backend.repository.ParentRepository;

@Service
public class ParentService {
    @Autowired
    private ParentRepository parentRepository;

    public Parent saveParent(Parent parent) {
        return parentRepository.save(parent);
    }

    public List<Parent> getAllParents() {
        return parentRepository.findAll();
    }

    public Parent getParentById(Long id) {
        return parentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent not found with id: " + id));
    }

    public Parent updateParent(Parent parent) {
        return parentRepository.save(parent);
    }

    public void deleteParent(Long id) {
        parentRepository.deleteById(id);
    }
}