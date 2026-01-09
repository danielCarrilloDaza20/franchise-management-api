package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.adapter;

import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import com.company.franchise.franchisemanagementapi.domain.port.BranchRepository;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.BranchEntity;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.mapper.BranchMapper;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.mapper.ProductMapper;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.repository.BranchR2dbcRepository;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.repository.ProductR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BranchRepositoryAdapter implements BranchRepository {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final BranchR2dbcRepository branchR2dbcRepository;
    private final ProductR2dbcRepository productR2dbcRepository;
    private final BranchMapper mapper;
    private final ProductMapper productMapper;

    @Override
    public Mono<Branch> create(Branch branch, UUID franchiseId) {
        BranchEntity entity = mapper.toEntity(branch, franchiseId);

        return r2dbcEntityTemplate.insert(BranchEntity.class)
                .using(entity)
                .map(mapper::toDomainBranchWithoutProducts);
    }

    @Override
    public Mono<Branch> findById(UUID franchiseId, UUID branchId) {

        return branchR2dbcRepository.findByIdAndFranchiseId(branchId, franchiseId)
                .flatMap(this::loadBranchWithProducts);
    }

    private Mono<Branch> loadBranchWithProducts(BranchEntity branchEntity) {
        return productR2dbcRepository.findByBranchId(branchEntity.getId())
                .map(productMapper::toDomainProduct)
                .collectList()
                .map(products -> mapper.toDomainBranch(branchEntity, products));
    }

    @Override
    public Mono<Branch> update(Branch branch, UUID franchiseId) {
        BranchEntity entity = mapper.toEntity(branch, franchiseId);

        return branchR2dbcRepository.save(entity)
                .map(mapper::toDomainBranchWithoutProducts);
    }
}
