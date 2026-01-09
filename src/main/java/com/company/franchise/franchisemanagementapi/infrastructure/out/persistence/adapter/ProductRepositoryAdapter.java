package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.adapter;

import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.domain.port.ProductRepository;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.ProductEntity;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.mapper.ProductMapper;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.repository.ProductR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {
    private final ProductMapper mapper;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final ProductR2dbcRepository productR2dbcRepository;


    @Override
    public Mono<Product> create(Product product, UUID branchId) {
        ProductEntity entity = mapper.toEntity(product, branchId);

        return r2dbcEntityTemplate.insert(ProductEntity.class)
                .using(entity)
                .map(mapper::toDomainBranchWithoutProducts);
    }

    @Override
    public Mono<Product> update(Product product, UUID branchId) {
        ProductEntity entity = mapper.toEntity(product, branchId);
        return productR2dbcRepository.save(entity)
                .map(mapper::toDomainProduct);
    }

    @Override
    public Mono<Product> findById(UUID branchId, UUID productId) {
        return productR2dbcRepository.findByIdAndBranchId(productId, branchId)
                .map(mapper::toDomainProduct);
    }

    @Override
    public Mono<Void> deleteById(UUID branchId, UUID productId) {
        return r2dbcEntityTemplate.delete(ProductEntity.class)
                .matching(Query.query(
                        Criteria.where("id").is(productId)
                                .and("branch_id").is(branchId)
                ))
                .all()
                .then();
    }
}
