package com.company.franchise.franchisemanagementapi.domain.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Data
public class Branch {
    private final UUID id;
    private String name;
    private final List<Product> products;

    public Branch(UUID id, String name) {
        this.id = id;
        this.name = name;
        this.products = new ArrayList<>();
    }

    public Branch(UUID id, String name, List<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public Product getTopStockProduct() {
        return products.stream()
                .max(Comparator.comparingInt(Product::getStock))
                .orElseThrow(() ->
                        new IllegalStateException("Branch has no products")
                );
    }

}
