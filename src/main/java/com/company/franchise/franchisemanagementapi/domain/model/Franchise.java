package com.company.franchise.franchisemanagementapi.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Franchise {
    private final UUID id;
    private String name;
    private final List<Branch> branches = new ArrayList<>();

    public Franchise(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addBranch(Branch branch) {
        this.branches.add(branch);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Branch> getBranches() {
        return branches;
    }
}
