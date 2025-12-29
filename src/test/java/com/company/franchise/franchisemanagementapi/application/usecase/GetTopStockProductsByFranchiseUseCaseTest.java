package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.domain.model.TopProductByBranch;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetTopStockProductsByFranchiseUseCaseTest {

    private FranchiseRepository repository;
    private GetTopStockProductsByFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(FranchiseRepository.class);
        useCase = new GetTopStockProductsByFranchiseUseCase(repository);
    }

    @Test
    void shouldReturnTopStockProductPerBranch() {
        Franchise franchise = new Franchise(UUID.randomUUID(), "Test Franchise");

        Branch branch1 = new Branch(UUID.randomUUID(), "Branch A");
        branch1.addProduct(new Product(UUID.randomUUID(), "Product A1", 10));
        branch1.addProduct(new Product(UUID.randomUUID(), "Product A2", 30));

        Branch branch2 = new Branch(UUID.randomUUID(), "Branch B");
        branch2.addProduct(new Product(UUID.randomUUID(), "Product B1", 5));
        branch2.addProduct(new Product(UUID.randomUUID(), "Product B2", 20));

        franchise.addBranch(branch1);
        franchise.addBranch(branch2);

        when(repository.findById(anyString()))
                .thenReturn(Mono.just(franchise));

        Mono<List<TopProductByBranch>> result =
                useCase.execute("franchise-id");

        StepVerifier.create(result)
                .assertNext(list -> {
                    assertEquals(2, list.size());

                    assertEquals("Branch A", list.get(0).getBranchName());
                    assertEquals(30, list.get(0).getProduct().getStock());

                    assertEquals("Branch B", list.get(1).getBranchName());
                    assertEquals(20, list.get(1).getProduct().getStock());
                })
                .verifyComplete();
    }
}

