package com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("franchises")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseEntity {

    @Id
    @Column("id")
    private UUID id;
    private String name;

    @CreatedDate
    private LocalDateTime createdAt;

}
