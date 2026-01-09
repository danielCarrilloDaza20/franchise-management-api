package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BusinessException;
import com.company.franchise.franchisemanagementapi.domain.exception.ProductNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.port.BranchRepository;
import com.company.franchise.franchisemanagementapi.domain.port.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemoveProductFromBranchUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    public Mono<Void> execute(UUID franchiseId, UUID branchId, UUID productId) {
        return branchRepository.existsInFranchise(branchId, franchiseId)
                .flatMap(exists -> {
                    if (!exists)
                        return Mono.error(new BusinessException("The branch does not belong to the indicated franchise."));

                    return productRepository.findById(branchId, productId)
                            .switchIfEmpty(Mono.error(new ProductNotFoundException(branchId, productId)))
                            .flatMap(product -> productRepository.deleteById(branchId, productId));
                });
    }
}
