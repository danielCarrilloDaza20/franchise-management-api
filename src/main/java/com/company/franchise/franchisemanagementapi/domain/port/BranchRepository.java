package com.company.franchise.franchisemanagementapi.domain.port;

import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BranchRepository {
    Mono<Branch> create(Branch branch, UUID franchiseId);

    Mono<Branch> findById(UUID franchiseId, UUID branchId);

    Mono<Branch> update(Branch branch, UUID franchiseId);

    Mono<Boolean> existsByNameInFranchise(String name, UUID franchiseId);

    Mono<Boolean> existsInFranchise(UUID branchId, UUID franchiseId);
}
