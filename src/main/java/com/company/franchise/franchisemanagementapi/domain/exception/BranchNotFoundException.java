package com.company.franchise.franchisemanagementapi.domain.exception;

import java.util.UUID;

public class BranchNotFoundException extends DomainException {
    public BranchNotFoundException(String branchId) {
        super("Branch not found with id: " + branchId);
    }

    public BranchNotFoundException(UUID branchId) {
        super("Branch not found with id: " + branchId);
    }

    public BranchNotFoundException(UUID franchiseId, UUID branchId) {
        super("Branch not found with id: " + branchId +
                " in franchise: " + franchiseId);
    }
}
