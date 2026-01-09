package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BusinessException;
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
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException("Franchise not found: " + franchiseId)))
                .flatMap(franchise -> branchRepository.existsByNameInFranchise(branchName, franchiseId)
                        .flatMap(exists -> {
                            if (exists) {
                                return Mono.error(new BusinessException("The franchise already has a branch called: " + branchName));
                            }
                            Branch branch = new Branch(UUID.randomUUID(), branchName);
                            return branchRepository.create(branch, franchiseId);
                        }))
                .then();
    }
}
