package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.repository;

import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductR2dbcRepository extends ReactiveCrudRepository<ProductEntity, String> {

    Flux<ProductEntity> findByBranchId(UUID id);

    Mono<ProductEntity> findByIdAndBranchId(UUID productId, UUID branchId);
}

