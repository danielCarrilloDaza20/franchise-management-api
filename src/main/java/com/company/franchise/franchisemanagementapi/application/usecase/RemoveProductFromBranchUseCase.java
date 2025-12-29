package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class RemoveProductFromBranchUseCase {
    private final FranchiseRepository repository;

    public RemoveProductFromBranchUseCase(FranchiseRepository repository) {
        this.repository = repository;
    }

    public Mono<Void> execute(
            String franchiseId,
            String branchId,
            String productId
    ) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new IllegalStateException("Franchise not found")
                ))
                .doOnNext(franchise ->
                        franchise.removeProductFromBranch(
                                UUID.fromString(branchId),
                                UUID.fromString(productId)
                        )
                )
                .flatMap(repository::save)
                .then();
    }
}
