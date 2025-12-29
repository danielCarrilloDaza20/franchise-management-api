package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.adapter;

import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.FranchiseEntity;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.mapper.FranchisePersistenceMapper;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.repository.BranchR2dbcRepository;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.repository.FranchiseR2dbcRepository;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.repository.ProductR2dbcRepository;
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

    public FranchiseRepositoryAdapter(
            FranchiseR2dbcRepository franchiseRepository,
            BranchR2dbcRepository branchRepository,
            ProductR2dbcRepository productRepository,
            FranchisePersistenceMapper mapper
    ) {
        this.franchiseRepository = franchiseRepository;
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Franchise> findById(String id) {
        UUID franchiseId = UUID.fromString(id);

        return franchiseRepository.findById(franchiseId)
                .flatMap(franchiseEntity ->
                        branchRepository.findByFranchiseId(franchiseId)
                                .flatMap(branchEntity ->
                                        productRepository.findByBranchId(branchEntity.getId())
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
    public Mono<Franchise> save(Franchise franchise) {

        FranchiseEntity franchiseEntity =
                mapper.toEntity(franchise);

        return franchiseRepository.save(franchiseEntity)
                .flatMap(savedFranchise ->
                        branchRepository
                                .deleteAll(
                                        branchRepository
                                                .findByFranchiseId(savedFranchise.getId())
                                )
                                .thenMany(
                                        Flux.fromIterable(
                                                mapper.toBranchEntities(franchise)
                                        )
                                )
                                .flatMap(branchRepository::save)
                                .collectList()
                                .flatMap(savedBranches ->
                                        productRepository
                                                .deleteAll(
                                                        productRepository
                                                                .findAll()
                                                )
                                                .thenMany(
                                                        Flux.fromIterable(
                                                                mapper.toProductEntities(franchise)
                                                        )
                                                )
                                                .flatMap(productRepository::save)
                                                .then(
                                                        findById(
                                                                savedFranchise.getId().toString()
                                                        )
                                                )
                                )
                );
    }
}
