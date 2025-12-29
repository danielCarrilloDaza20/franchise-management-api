package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.repository;

import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.FranchiseEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FranchiseR2dbcRepository extends ReactiveCrudRepository<FranchiseEntity, UUID> {
}

