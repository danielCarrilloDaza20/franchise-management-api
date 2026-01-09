package com.company.franchise.franchisemanagementapi.infrastructure.in.dto;

import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
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
public class FranchiseResponse {
    private UUID id;
    private String name;
    private List<BranchResponse> branches;

    public static FranchiseResponse fromDomain(Franchise franchise) {
        return FranchiseResponse.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .branches(franchise.getBranches() != null
                        ? franchise.getBranches().stream()
                        .map(BranchResponse::fromDomain)
                        .toList()
                        : Collections.emptyList())
                .build();
    }
}
