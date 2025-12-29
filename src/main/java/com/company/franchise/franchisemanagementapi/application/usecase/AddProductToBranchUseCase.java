package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class AddProductToBranchUseCase {

    private final FranchiseRepository repository;

    public AddProductToBranchUseCase(FranchiseRepository repository) {
        this.repository = repository;
    }

    public Mono<Void> execute(String franchiseId, String branchId, String productName, int stock) {
        return repository.findById(franchiseId)
                .map(franchise -> {
                    franchise.addProductToBranch(
                            branchId,
                            new Product(UUID.randomUUID(), productName, stock)
                    );
                    return franchise;
                })
                .flatMap(repository::save)
                .then();
    }
}

