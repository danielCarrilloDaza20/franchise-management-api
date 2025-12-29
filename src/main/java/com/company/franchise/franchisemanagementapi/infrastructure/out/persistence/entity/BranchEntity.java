package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("branches")
public class BranchEntity {

    @Id
    private UUID id;

    @Column("name")
    private String name;

    @Column("franchise_id")
    private UUID franchiseId;

    public BranchEntity(UUID id, String name, UUID franchiseId) {
        this.id = id;
        this.name = name;
        this.franchiseId = franchiseId;
    }

    public BranchEntity() {}

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getFranchiseId() {
        return franchiseId;
    }
}
