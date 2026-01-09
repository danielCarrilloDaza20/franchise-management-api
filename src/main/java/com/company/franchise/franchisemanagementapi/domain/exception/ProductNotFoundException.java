package com.company.franchise.franchisemanagementapi.domain.exception;

import java.util.UUID;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(String productId) {
        super("Product not found with id: " + productId);
    }

    public ProductNotFoundException(UUID productId) {
        super("Product not found with id: " + productId);
    }

    public ProductNotFoundException(UUID branchId, UUID productId) {
        super("Product not found with id: " + productId +
                " in branch: " + branchId);
    }
}
