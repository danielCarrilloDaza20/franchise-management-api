package com.company.franchise.franchisemanagementapi.domain.port;

import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FranchiseRepository {
    Mono<Franchise> create(Franchise franchise);

    Mono<Franchise> update(Franchise franchise);

    Mono<Franchise> findById(UUID id);

    Flux<Franchise> findAll(Pageable pageable);

    Mono<Boolean> existsByName(String name);
}
