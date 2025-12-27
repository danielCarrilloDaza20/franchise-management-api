package com.company.franchise.franchisemanagementapi.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Branch {
    private final UUID id;
    private String name;
    private final List<Product> products = new ArrayList<>();

    public Branch(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return products;
    }
}
