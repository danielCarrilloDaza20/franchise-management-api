package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("franchises")
public class FranchiseEntity {

    @Id
    private UUID id;

    @Column("name")
    private String name;

    public FranchiseEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public FranchiseEntity() {}

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
