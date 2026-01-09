package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BranchNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.exception.InvalidStockException;
import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.domain.port.BranchRepository;
import com.company.franchise.franchisemanagementapi.domain.port.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddProductToBranchUseCase {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Mono<Void> execute(UUID franchiseId, UUID branchId,
                              String productName, int stock) {

        if (stock < 0) {
            return Mono.error(new InvalidStockException(stock));
        }

        return branchRepository.findById(franchiseId, branchId)
                .switchIfEmpty(Mono.error(
                        new BranchNotFoundException(franchiseId, branchId)
                ))
                .flatMap(branch -> {
                    Product product = new Product(UUID.randomUUID(), productName, stock);

                    return productRepository.create(product, branchId);
                })
                .then();
    }
}

