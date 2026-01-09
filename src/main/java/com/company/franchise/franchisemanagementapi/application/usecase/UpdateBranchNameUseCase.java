package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BranchNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.port.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateBranchNameUseCase {
    private final BranchRepository branchRepository;

    public Mono<Void> execute(UUID franchiseId, UUID branchId, String newName){
        return branchRepository.findById(franchiseId, branchId)
                .switchIfEmpty(Mono.error(
                        new BranchNotFoundException("Branch not found: " + branchId)
                )).flatMap(branch -> {
                    branch.updateName(newName);
                    return branchRepository.update(branch, franchiseId);
                })
                .then();
    }
}
