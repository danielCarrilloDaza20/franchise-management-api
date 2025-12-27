package com.company.franchise.franchisemanagementapi.domain.port;

import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(String id);
}
