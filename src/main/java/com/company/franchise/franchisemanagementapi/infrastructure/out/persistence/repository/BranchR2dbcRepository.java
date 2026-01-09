package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.repository;

import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.BranchEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface BranchR2dbcRepository extends ReactiveCrudRepository<BranchEntity, UUID> {

    Flux<BranchEntity> findByFranchiseId(UUID franchiseId);

    Mono<BranchEntity> findByIdAndFranchiseId(UUID id, UUID franchiseId);
}

