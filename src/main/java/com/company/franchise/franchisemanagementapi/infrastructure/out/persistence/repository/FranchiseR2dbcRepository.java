package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.repository;

import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.FranchiseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FranchiseR2dbcRepository extends ReactiveCrudRepository<FranchiseEntity, UUID> {
    Mono<FranchiseEntity> findByName(String name);

    Flux<FranchiseEntity> findAllBy(Pageable pageable);
}

