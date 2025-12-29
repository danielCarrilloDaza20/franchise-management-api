package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UpdateProductStockUseCaseTest {

    private FranchiseRepository repository;
    private UpdateProductStockUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(FranchiseRepository.class);
        useCase = new UpdateProductStockUseCase(repository);
    }

    @Test
    void shouldUpdateProductStock() {
        Franchise franchise = new Franchise(UUID.randomUUID(), "Test Franchise");
        Branch branch = new Branch(UUID.randomUUID(), "Branch 1");
        Product product = new Product(UUID.randomUUID(), "Laptop", 5);

        branch.addProduct(product);
        franchise.addBranch(branch);

        when(repository.findById(anyString()))
                .thenReturn(Mono.just(franchise));

        when(repository.save(any(Franchise.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Void> result = useCase.execute(
                "franchise-id",
                branch.getId().toString(),
                product.getId().toString(),
                20
        );

        StepVerifier.create(result)
                .verifyComplete();

        verify(repository).save(franchise);
        assertEquals(20, product.getStock());
    }
}

