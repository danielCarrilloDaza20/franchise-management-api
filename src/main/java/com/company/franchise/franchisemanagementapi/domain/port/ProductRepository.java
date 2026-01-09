package com.company.franchise.franchisemanagementapi.domain.port;

import com.company.franchise.franchisemanagementapi.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductRepository {
    Mono<Product> create(Product product, UUID branchId);

    Mono<Product> update(Product product, UUID branchId);

    Mono<Product> findById(UUID branchId, UUID productId);

    Mono<Void> deleteById(UUID branchId, UUID productId);
}
