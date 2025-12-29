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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RemoveProductFromBranchUseCaseTest {

    private FranchiseRepository repository;
    private RemoveProductFromBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(FranchiseRepository.class);
        useCase = new RemoveProductFromBranchUseCase(repository);
    }

    @Test
    void shouldRemoveProductFromBranch() {
        Franchise franchise = new Franchise(
                UUID.randomUUID(), "Test Franchise"
        );

        Branch branch = new Branch(
                UUID.randomUUID(), "Main Branch"
        );

        Product product = new Product(
                UUID.randomUUID(), "Laptop", 10
        );

        branch.addProduct(product);
        franchise.addBranch(branch);

        when(repository.findById(anyString()))
                .thenReturn(Mono.just(franchise));

        when(repository.save(any(Franchise.class)))
                .thenAnswer(invocation ->
                        Mono.just(invocation.getArgument(0))
                );

        Mono<Void> result = useCase.execute(
                franchise.getId().toString(),
                branch.getId().toString(),
                product.getId().toString()
        );

        StepVerifier.create(result)
                .verifyComplete();

        assertTrue(branch.getProducts().isEmpty());
        verify(repository).save(franchise);
    }
}

