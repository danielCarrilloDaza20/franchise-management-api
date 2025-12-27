package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class AddBranchToFranchiseUseCase {
    private final FranchiseRepository repository;

    public AddBranchToFranchiseUseCase(FranchiseRepository repository) {
        this.repository = repository;
    }

    public Mono<Void> execute(String franchiseId, String branchName){
        return repository.findById(franchiseId)
                .map( franchise -> {
                    franchise.addBranch(new Branch(UUID.randomUUID(), branchName));
                    return franchise;
                })
                .flatMap(repository::save)
                .then();
    }
}
