package com.company.franchise.franchisemanagementapi.domain.model;

import java.util.UUID;

public class Product {
    private final UUID id;
    private String name;
    private int stock;

    public Product(UUID id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    public void updateStock(int newStock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.stock = newStock;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }
}
