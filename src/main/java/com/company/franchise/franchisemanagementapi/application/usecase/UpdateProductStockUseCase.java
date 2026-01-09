package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BranchNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.exception.InvalidStockException;
import com.company.franchise.franchisemanagementapi.domain.exception.ProductNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.port.BranchRepository;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import com.company.franchise.franchisemanagementapi.domain.port.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateProductStockUseCase {

    private final FranchiseRepository repository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Mono<Void> execute(UUID franchiseId, UUID branchId, UUID productId, int newStock) {
        if (newStock < 0) {
            return Mono.error(new InvalidStockException(newStock));
        }

        return branchRepository.findById(franchiseId, branchId)
                .switchIfEmpty(Mono.error(
                        new BranchNotFoundException("Franchise not found: " + franchiseId)
                )).flatMap(branch -> productRepository.findById(branchId, productId)
                        .switchIfEmpty(Mono.error(
                                new ProductNotFoundException(branchId, productId)
                        ))
                        .flatMap(product -> {
                            product.updateStock(newStock);
                            return productRepository.update(product, branchId);
                        })

                )
                .then();
    }
}

