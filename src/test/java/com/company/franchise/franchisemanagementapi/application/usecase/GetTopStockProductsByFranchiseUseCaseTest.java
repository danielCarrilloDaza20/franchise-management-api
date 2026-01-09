package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BusinessException;
import com.company.franchise.franchisemanagementapi.domain.exception.FranchiseNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.domain.model.TopProductByBranch;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetTopStockProductsByFranchiseUseCaseTest {

    private FranchiseRepository repository;
    private GetTopStockProductsByFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(FranchiseRepository.class);
        useCase = new GetTopStockProductsByFranchiseUseCase(repository);
    }

    @Test
    void shouldReturnTopStockProductPerBranch() {
        UUID franchiseId = UUID.randomUUID();
        Franchise franchise = new Franchise(franchiseId, "Test Franchise");

        Branch branch1 = new Branch(UUID.randomUUID(), "Branch A");
        branch1.addProduct(new Product(UUID.randomUUID(), "Product A1", 10));
        branch1.addProduct(new Product(UUID.randomUUID(), "Product A2", 30));

        Branch branch2 = new Branch(UUID.randomUUID(), "Branch B");
        branch2.addProduct(new Product(UUID.randomUUID(), "Product B1", 5));
        branch2.addProduct(new Product(UUID.randomUUID(), "Product B2", 20));

        franchise.addBranch(branch1);
        franchise.addBranch(branch2);

        when(repository.findById(franchiseId)).thenReturn(Mono.just(franchise));

        Mono<List<TopProductByBranch>> result = useCase.execute(franchiseId);

        StepVerifier.create(result)
                .assertNext(list -> {
                    assertEquals(2, list.size());

                    assertTrue(list.stream().anyMatch(t ->
                            t.getBranchName().equals("Branch A") && t.getProduct().getStock() == 30));

                    assertTrue(list.stream().anyMatch(t ->
                            t.getBranchName().equals("Branch B") && t.getProduct().getStock() == 20));
                })
                .verifyComplete();
    }

    @Test
    void shouldFailWhenFranchiseHasNoBranches() {
        UUID franchiseId = UUID.randomUUID();
        Franchise emptyFranchise = new Franchise(franchiseId, "Empty Franchise");

        when(repository.findById(franchiseId)).thenReturn(Mono.just(emptyFranchise));

        Mono<List<TopProductByBranch>> result = useCase.execute(franchiseId);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().contains("has no registered branches"))
                .verify();
    }

    @Test
    void shouldFailWhenFranchiseDoesNotExist() {
        UUID franchiseId = UUID.randomUUID();
        when(repository.findById(franchiseId)).thenReturn(Mono.empty());

        Mono<List<TopProductByBranch>> result = useCase.execute(franchiseId);

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }
}