package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.mapper;

import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.BranchEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class BranchMapper {
    public Branch toDomainBranchWithoutProducts(BranchEntity branchEntity) {
        return new Branch(branchEntity.getId(), branchEntity.getName(), List.of());
    }

    public BranchEntity toEntity(Branch branch, UUID franchiseId) {
        return new BranchEntity(branch.getId(), franchiseId, branch.getName());
    }

    public Branch toDomainBranch(BranchEntity branchEntity, List<Product> products) {
        return new Branch(branchEntity.getId(), branchEntity.getName(), products);
    }
}
