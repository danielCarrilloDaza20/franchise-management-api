package com.company.franchise.franchisemanagementapi.infrastructure.in.dto;

import com.company.franchise.franchisemanagementapi.domain.model.TopProductByBranch;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopProductByBranchResponse {

    private String branchName;
    private String productName;
    private int stock;

    public static TopProductByBranchResponse fromDomain(TopProductByBranch domain) {
        return new TopProductByBranchResponse(
                domain.getBranchName(),
                domain.getProduct().getName(),
                domain.getProduct().getStock()
        );
    }
}
