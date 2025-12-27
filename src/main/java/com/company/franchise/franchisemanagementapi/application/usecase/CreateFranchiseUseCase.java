package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class CreateFranchiseUseCase {
    private final FranchiseRepository repository;

    public CreateFranchiseUseCase(FranchiseRepository repository) {
        this.repository = repository;
    }

    public Mono<Franchise> execute(String name) {
        Franchise franchise = new Franchise(UUID.randomUUID(), name);
        return repository.save(franchise);
    }
}
