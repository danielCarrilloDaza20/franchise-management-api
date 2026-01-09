package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.FranchiseNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.model.TopProductByBranch;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetTopStockProductsByFranchiseUseCase {
    private final FranchiseRepository repository;

    public Mono<List<TopProductByBranch>> execute(UUID franchiseId) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException("Franchise not found with id: " + franchiseId)))
                .map(Franchise::getTopStockProductsByBranch);
    }
}
