package com.company.franchise.franchisemanagementapi.domain.exception;

import java.util.UUID;

public class FranchiseNotFoundException extends DomainException {

    public FranchiseNotFoundException(String franchiseId) {
        super("Franchise not found with id: " + franchiseId);
    }

    public FranchiseNotFoundException(UUID franchiseId) {
        super("Franchise not found with id: " + franchiseId);
    }
}
