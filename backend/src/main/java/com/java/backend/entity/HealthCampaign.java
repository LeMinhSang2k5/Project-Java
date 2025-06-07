package com.java.backend.entity;

import java.time.LocalDate;

import com.java.backend.enums.CampainType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "health_campaigns")
public class HealthCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // "Tiêm chủng Sởi-Quai bị-Rubella 2024", "Khám sức khỏe định kỳ học kỳ 1"

    @Enumerated(EnumType.STRING)
    private CampainType type;

    private LocalDate scheduledDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Getters and Setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CampainType getType() {
        return type;
    }

    public void setType(CampainType type) {
        this.type = type;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
