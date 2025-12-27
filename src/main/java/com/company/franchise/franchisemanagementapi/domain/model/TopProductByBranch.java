package com.company.franchise.franchisemanagementapi.domain.model;

public class TopProductByBranch {
    private final String branchId;
    private final String branchName;
    private final Product product;

    public TopProductByBranch(String branchId, String branchName, Product product) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.product = product;
    }

    public String getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public Product getProduct() {
        return product;
    }
}
