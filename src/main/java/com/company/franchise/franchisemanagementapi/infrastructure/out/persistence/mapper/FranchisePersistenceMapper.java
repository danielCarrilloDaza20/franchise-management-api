package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.mapper;

import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.BranchEntity;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.FranchiseEntity;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FranchisePersistenceMapper {

    /* =========================
       DOMAIN <- ENTITY
       ========================= */

    public Franchise toDomainFranchise(
            FranchiseEntity entity,
            List<Branch> branches
    ) {
        Franchise franchise = new Franchise(
                entity.getId(),
                entity.getName()
        );

        branches.forEach(franchise::addBranch);
        return franchise;
    }

    public Branch toDomainBranch(
            BranchEntity entity,
            List<ProductEntity> productEntities
    ) {
        Branch branch = new Branch(
                entity.getId(),
                entity.getName()
        );

        productEntities.stream()
                .map(this::toDomainProduct)
                .forEach(branch::addProduct);

        return branch;
    }

    private Product toDomainProduct(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getStock()
        );
    }

    /* =========================
       ENTITY <- DOMAIN
       ========================= */

    public FranchiseEntity toEntity(Franchise domain) {
        return new FranchiseEntity(
                domain.getId(),
                domain.getName()
        );
    }

    public List<BranchEntity> toBranchEntities(Franchise domain) {
        return domain.getBranches().stream()
                .map(branch ->
                        new BranchEntity(
                                branch.getId(),
                                branch.getName(),
                                domain.getId()
                        )
                )
                .toList();
    }

    public List<ProductEntity> toProductEntities(Franchise domain) {
        return domain.getBranches().stream()
                .flatMap(branch ->
                        branch.getProducts().stream()
                                .map(product ->
                                        new ProductEntity(
                                                product.getId(),
                                                product.getName(),
                                                product.getStock(),
                                                branch.getId()
                                        )
                                )
                )
                .toList();
    }
}


