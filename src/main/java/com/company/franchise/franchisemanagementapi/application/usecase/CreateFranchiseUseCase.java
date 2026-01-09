package com.company.franchise.franchisemanagementapi.application.usecase;

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
        Franchise franchise = new Franchise(name);
        return repository.create(franchise);
    }
}
