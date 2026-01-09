package com.company.franchise.franchisemanagementapi.domain.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Franchise {
    private final UUID id;
    private String name;
    private final List<Branch> branches;

    public Franchise(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.branches = new ArrayList<>();
    }

    public Franchise(UUID id, String name) {
        this.id = id;
        this.name = name;
        this.branches = new ArrayList<>();
    }

    public Franchise(UUID id, String name, List<Branch> branches) {
        this.id = id;
        this.name = name;
        this.branches = branches;
    }

    public void addBranch(Branch branch) {
        this.branches.add(branch);
    }

    public List<TopProductByBranch> getTopStockProductsByBranch() {
        return branches.stream()
                .filter(branch -> !branch.getProducts().isEmpty())
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

    public void updateName(String newName) {
        this.name = newName;
    }
}
