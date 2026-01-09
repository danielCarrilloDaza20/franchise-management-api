package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BusinessException;
import com.company.franchise.franchisemanagementapi.domain.exception.ProductNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.port.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UpdateProductNameUseCase {
    private final ProductRepository productRepository;

    public Mono<Void> execute(UUID branchId, UUID productId, String newName) {
        return productRepository.existsByNameInBranch(newName, branchId)
                .flatMap(exists -> {
                    if (exists)
                        return Mono.error(new BusinessException("There is already a product with that name in this branch."));
                    return productRepository.findById(branchId, productId);
                })
                .switchIfEmpty(Mono.error(new ProductNotFoundException(productId)))
                .flatMap(product -> {
                    product.setName(newName);
                    return productRepository.update(product, branchId);
                }).then();
    }
}
