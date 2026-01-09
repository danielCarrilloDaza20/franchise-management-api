package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("branches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchEntity {

    @Id
    private UUID id;

    @Column("franchise_id")
    private UUID franchiseId;
    private String name;
}