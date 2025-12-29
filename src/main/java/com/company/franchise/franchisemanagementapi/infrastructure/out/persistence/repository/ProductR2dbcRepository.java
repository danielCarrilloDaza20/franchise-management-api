package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.repository;

import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface ProductR2dbcRepository extends ReactiveCrudRepository<ProductEntity, UUID> {

    Flux<ProductEntity> findByBranchId(UUID branchId);
}

