package com.company.franchise.franchisemanagementapi.infrastructure.in.dto;

import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponse {
    private UUID id;
    private String name;
    private List<ProductResponse> products;

    public static BranchResponse fromDomain(Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .products(branch.getProducts() != null
                        ? branch.getProducts().stream()
                        .map(ProductResponse::fromDomain)
                        .toList()
                        : Collections.emptyList())
                .build();
    }
}
