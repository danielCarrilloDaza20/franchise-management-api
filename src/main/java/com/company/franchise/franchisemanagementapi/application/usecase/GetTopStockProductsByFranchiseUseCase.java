package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.model.TopProductByBranch;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GetTopStockProductsByFranchiseUseCase {
    private final FranchiseRepository repository;

    public GetTopStockProductsByFranchiseUseCase(FranchiseRepository repository) {
        this.repository = repository;
    }

    public Mono<List<TopProductByBranch>> execute(String franchiseId) {
        return repository.findById(franchiseId)
                .map(Franchise::getTopStockProductsByBranch);
    }
}
