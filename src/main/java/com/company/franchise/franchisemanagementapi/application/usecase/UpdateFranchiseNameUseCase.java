package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BranchNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateFranchiseNameUseCase {
    private final FranchiseRepository franchiseRepository;

    public Mono<Void> execute(UUID franchiseId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new BranchNotFoundException("Franchise not found: " + franchiseId)
                )).flatMap(franchise -> {
                    franchise.updateName(newName);
                    return franchiseRepository.update(franchise);
                })
                .then();

    }
}
