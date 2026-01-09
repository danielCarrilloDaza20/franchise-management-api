package com.company.franchise.franchisemanagementapi.infrastructure.in.dto;

import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.domain.model.TopProductByBranch;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class TopProductByBranchResponse {

    private String branchName;
    private String productName;
    private int stock;

    public static TopProductByBranchResponse fromDomain(TopProductByBranch domain) {
        return new TopProductByBranchResponse(
                domain.getBranchName(),
                Optional.ofNullable(domain.getProduct()).map(Product::getName).orElse("N/A"),
                Optional.ofNullable(domain.getProduct()).map(Product::getStock).orElse(0)
        );
    }
}
