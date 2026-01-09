package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.adapter;

import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.FranchiseEntity;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.mapper.FranchisePersistenceMapper;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.repository.BranchR2dbcRepository;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.repository.FranchiseR2dbcRepository;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.repository.ProductR2dbcRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class FranchiseRepositoryAdapter implements FranchiseRepository {

    private final FranchiseR2dbcRepository franchiseRepository;
    private final BranchR2dbcRepository branchRepository;
    private final ProductR2dbcRepository productRepository;
    private final FranchisePersistenceMapper mapper;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public FranchiseRepositoryAdapter(
            FranchiseR2dbcRepository franchiseRepository,
            BranchR2dbcRepository branchRepository,
            ProductR2dbcRepository productRepository,
            FranchisePersistenceMapper mapper,
            R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.franchiseRepository = franchiseRepository;
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @Override
    public Flux<Franchise> findAll(Pageable pageable) {
        return franchiseRepository.findAllBy(pageable)
                .flatMap(franchiseEntity ->
                        branchRepository.findByFranchiseId(franchiseEntity.getId())
                                .flatMap(branchEntity ->
                                        productRepository.findByBranchId(branchEntity.getId())
                                                .map(mapper::toDomainProduct)
                                                .collectList()
                                                .map(products -> mapper.toDomainBranch(branchEntity, products)))
                                .collectList()
                                .map(branches -> mapper.toDomainFranchise(franchiseEntity, branches)));

    }

    @Override
    public Mono<Franchise> findById(UUID id) {

        return franchiseRepository.findById(id)
                .flatMap(franchiseEntity ->
                        branchRepository.findByFranchiseId(id)
                                .flatMap(branchEntity ->
                                        productRepository.findByBranchId(branchEntity.getId())
                                                .map(mapper::toDomainProduct)
                                                .collectList()
                                                .map(products ->
                                                        mapper.toDomainBranch(
                                                                branchEntity,
                                                                products
                                                        )
                                                )
                                )
                                .collectList()
                                .map(branches ->
                                        mapper.toDomainFranchise(
                                                franchiseEntity,
                                                branches
                                        )
                                )
                );
    }

    @Override
    public Mono<Franchise> create(Franchise franchise) {

        FranchiseEntity entity = mapper.toEntity(franchise);

        return r2dbcEntityTemplate.insert(FranchiseEntity.class)
                .using(entity)
                .map(mapper::toDomainFranchiseWithoutBranches);
    }

    @Override
    public Mono<Franchise> update(Franchise franchise) {

        FranchiseEntity entity = mapper.toEntity(franchise);
        return franchiseRepository.save(entity)
                .map(mapper::toDomainFranchiseWithoutBranches);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return r2dbcEntityTemplate.select(FranchiseEntity.class)
                .from("franchises")
                .matching(Query.query(Criteria.where("name").is(name)))
                .exists();
    }
}

