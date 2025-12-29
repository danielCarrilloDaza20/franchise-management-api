package com.company.franchise.franchisemanagementapi.infrastructure.in.mapper;

import com.company.franchise.franchisemanagementapi.domain.model.TopProductByBranch;
import com.company.franchise.franchisemanagementapi.infrastructure.in.dto.TopProductByBranchResponse;
import org.springframework.stereotype.Component;

@Component
public class FranchiseMapper {

    public TopProductByBranchResponse toResponse(
            TopProductByBranch domain
    ) {
        return new TopProductByBranchResponse(
                domain.getBranchName(),
                domain.getProduct().getName(),
                domain.getProduct().getStock()
        );
    }
}

