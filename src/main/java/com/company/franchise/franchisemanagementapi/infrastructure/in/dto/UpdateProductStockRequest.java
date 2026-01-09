package com.company.franchise.franchisemanagementapi.infrastructure.in.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductStockRequest {
    @Min(value = 0, message = "Stock cannot be negative.")
    private int stock;
}
