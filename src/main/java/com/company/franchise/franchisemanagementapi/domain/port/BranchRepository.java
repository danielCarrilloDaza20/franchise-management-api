package com.company.franchise.franchisemanagementapi.domain.port;

import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BranchRepository {
    Mono<Branch> create(Branch branch, UUID franchiseId);

    Mono<Branch> findById(UUID franchiseId, UUID branchId);
}
