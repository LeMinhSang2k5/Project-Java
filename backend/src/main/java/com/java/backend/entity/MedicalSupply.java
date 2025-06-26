package com.java.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "medical_supplies")
public class MedicalSupply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity; // Số lượng tồn kho

    @Column(nullable = false)
    private String category; // Thuốc, vật tư y tế

    @Column(nullable = true)
    private LocalDate expiryDate; // Ngày hạn sử dụng

    @CreationTimestamp
    @Column(name = "stock_in_date", nullable = true, updatable = false)
    private LocalDateTime stockInDate; // Ngày nhập kho

    @UpdateTimestamp
    @Column(name = "stock_out_date", nullable = true)
    private LocalDateTime stockOutDate; // Ngày xuất kho

    // Constructors
    public MedicalSupply() {
        // JPA requires a default constructor
    }

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getStockInDate() {
        return stockInDate;
    }

    public void setStockInDate(LocalDateTime stockInDate) {
        this.stockInDate = stockInDate;
    }

    public LocalDateTime getStockOutDate() {
        return stockOutDate;
    }

    public void setStockOutDate(LocalDateTime stockOutDate) {
        this.stockOutDate = stockOutDate;
    }
}