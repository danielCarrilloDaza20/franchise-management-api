package com.company.franchise.franchisemanagementapi.domain.model;

import java.util.ArrayList;
import java.util.Comparator;
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

    public void updateProductStock(String productId, int stock) {
        Product product = products.stream()
                .filter(p -> p.getId().toString().equals(productId))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Product " + productId + " not found")
                );

        product.updateStock(stock);
    }

    public Product getTopStockProduct() {
        return products.stream()
                .max(Comparator.comparingInt(Product::getStock))
                .orElseThrow(() ->
                        new IllegalStateException("Branch has no products")
                );
    }

}
