package com.company.franchise.franchisemanagementapi.infrastructure.in.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductRequest {
    private String name;
    private int stock;
}
