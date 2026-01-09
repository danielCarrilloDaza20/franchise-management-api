package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.FranchiseNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import com.company.franchise.franchisemanagementapi.domain.port.BranchRepository;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddBranchToFranchiseUseCase {
    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;

    public Mono<Void> execute(UUID franchiseId, String branchName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new FranchiseNotFoundException("Franchise not found: " + franchiseId)
                ))
                .flatMap(franchise -> {
                    Branch branch = new Branch(UUID.randomUUID(), branchName);

                    return branchRepository.create(branch, franchiseId)
                            .doOnSuccess(franchise::addBranch);
                })
                .then();
    }
}
