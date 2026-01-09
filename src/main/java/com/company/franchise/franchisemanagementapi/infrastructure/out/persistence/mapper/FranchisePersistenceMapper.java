package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.mapper;

import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.BranchEntity;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.FranchiseEntity;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class FranchisePersistenceMapper {

    public Franchise toDomainFranchise(
            FranchiseEntity entity,
            List<Branch> branches
    ) {
        return new Franchise(
                entity.getId(),
                entity.getName(),
                branches
        );
    }

    public Franchise toDomainFranchiseWithoutBranches(
            FranchiseEntity entity
    ) {
        return new Franchise(
                entity.getId(),
                entity.getName(),
                List.of()
        );
    }

    public Branch toDomainBranch(
            BranchEntity entity,
            List<Product> products
    ) {
        Branch branch = new Branch(
                entity.getId(),
                entity.getName()
        );

        products.forEach(branch::addProduct);

        return branch;
    }

    public FranchiseEntity toEntity(Franchise franchise) {
        return new FranchiseEntity(
                franchise.getId(),
                franchise.getName(),
                null
        );
    }

    public Product toDomainProduct(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getStock()
        );
    }

    public List<BranchEntity> toBranchEntities(Franchise franchise) {
        return franchise.getBranches()
                .stream()
                .map(branch -> new BranchEntity(
                        branch.getId(),
                        franchise.getId(),
                        branch.getName()
                ))
                .toList();
    }


    public List<ProductEntity> toProductEntities(Franchise franchise) {
        return franchise.getBranches()
                .stream()
                .flatMap(branch ->
                        branch.getProducts()
                                .stream()
                                .map(product -> new ProductEntity(
                                        product.getId(),
                                        branch.getId(),
                                        product.getName(),
                                        product.getStock()
                                ))
                )
                .toList();
    }

    public BranchEntity toBranchEntity(Branch branch, UUID franchiseId) {
        if (branch == null) return null;
        return new BranchEntity(
                branch.getId(),
                franchiseId,
                branch.getName()
        );
    }


}



