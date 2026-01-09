package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BusinessException;
import com.company.franchise.franchisemanagementapi.domain.exception.FranchiseNotFoundException;
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
        return franchiseRepository.existsByName(newName)
                .flatMap(exists -> {
                    if (exists)
                        return Mono.error(new BusinessException("There is already another franchise with that name."));
                    return franchiseRepository.findById(franchiseId);
                })
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    franchise.updateName(newName);
                    return franchiseRepository.update(franchise);
                }).then();
    }
}
