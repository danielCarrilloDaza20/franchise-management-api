package com.company.franchise.franchisemanagementapi.infrastructure.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateFranchiseRequest {
    @NotBlank(message = "Name couldn't be empty.")
    @Size(min = 3, max = 50)
    private String name;
}
