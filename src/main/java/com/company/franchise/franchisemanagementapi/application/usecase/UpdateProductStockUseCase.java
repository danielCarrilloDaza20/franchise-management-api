package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateProductStockUseCase {

    private final FranchiseRepository repository;

    public UpdateProductStockUseCase(FranchiseRepository repository) {
        this.repository = repository;
    }

    public Mono<Void> execute(String franchiseId, String branchId, String productId, int stock) {
        return repository.findById(franchiseId)
                .map(franchise -> {
                    franchise.updateProductStock(branchId, productId, stock);
                    return franchise;
                })
                .flatMap(repository::save)
                .then();
    }
}

