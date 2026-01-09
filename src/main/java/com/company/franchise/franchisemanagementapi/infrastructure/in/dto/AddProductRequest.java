package com.company.franchise.franchisemanagementapi.infrastructure.in.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductRequest {
    @NotBlank(message = "Name couldn't be empty.")
    @Size(min = 3, max = 50)
    private String name;
    @Min(value = 0, message = "Initial stock couldn't be a negative number.")
    private int stock;
}
