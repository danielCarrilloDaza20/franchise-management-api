package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BusinessException;
import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateFranchiseUseCase {
    private final FranchiseRepository repository;

    public Mono<Franchise> execute(String name) {
        return repository.existsByName(name)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BusinessException("There is already a franchise with the name: " + name));
                    }
                    Franchise franchise = new Franchise(name);
                    return repository.create(franchise);
                });
    }
}
