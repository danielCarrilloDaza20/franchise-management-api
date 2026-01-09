package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.mapper;

import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductMapper {

    public ProductEntity toEntity(Product product, UUID branchId) {
        return new ProductEntity(product.getId(), branchId, product.getName(), product.getStock());
    }

    public Product toDomainBranchWithoutProducts(ProductEntity productEntity) {
        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getStock());
    }

    public Product toDomainProduct(ProductEntity productEntity) {
        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getStock());
    }
}
