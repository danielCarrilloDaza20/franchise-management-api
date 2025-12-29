package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("products")
public class ProductEntity {

    @Id
    private UUID id;

    @Column("name")
    private String name;

    @Column("stock")
    private int stock;

    @Column("branch_id")
    private UUID branchId;

    public ProductEntity(UUID id, String name, int stock, UUID branchId) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.branchId = branchId;
    }

    public ProductEntity() {}

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public UUID getBranchId() {
        return branchId;
    }
}
