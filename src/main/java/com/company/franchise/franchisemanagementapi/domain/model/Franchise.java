package com.company.franchise.franchisemanagementapi.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Franchise {
    private final UUID id;
    private String name;
    private final List<Branch> branches = new ArrayList<>();

    public Franchise(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addBranch(Branch branch) {
        this.branches.add(branch);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void addProductToBranch(String branchId, Product product) {
        Branch branch = branches.stream().filter(b -> b.getId().toString().equals(branchId))
                .findFirst()
                .orElseThrow(() -> {
                    return new IllegalStateException("Branch " + branchId + " not found");
                });

        branch.addProduct(product);
    }

    public void updateProductStock(String branchId, String productId, int stock) {
        Branch branch = branches.stream()
                .filter(b -> b.getId().toString().equals(branchId))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Branch " + branchId + " not found")
                );

        branch.updateProductStock(productId, stock);
    }

    public List<TopProductByBranch> getTopStockProductsByBranch() {
        return branches.stream()
                .map(branch -> {
                    Product topProduct = branch.getTopStockProduct();
                    return new TopProductByBranch(
                            branch.getId().toString(),
                            branch.getName(),
                            topProduct
                    );
                })
                .toList();
    }

    public void removeProductFromBranch(UUID branchId, UUID productId) {
        Branch branch = branches.stream()
                .filter(b -> b.getId().equals(branchId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Branch " + branchId + " not found"
                ));

        branch.removeProduct(productId);
    }
}
