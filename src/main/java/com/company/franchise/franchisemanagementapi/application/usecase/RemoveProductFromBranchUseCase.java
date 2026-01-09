package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.ProductNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.port.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemoveProductFromBranchUseCase {

    private final ProductRepository productRepository;

    public Mono<Void> execute(UUID franchiseId, UUID branchId, UUID productId) {

        return productRepository.findById(branchId, productId)
                .switchIfEmpty(Mono.error(
                        new ProductNotFoundException(branchId, productId)
                ))
                .flatMap(product ->
                        productRepository.deleteById(branchId, productId)
                );
    }
}
