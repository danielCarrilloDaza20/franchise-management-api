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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AddProductToBranchUseCaseTest {
    private FranchiseRepository repository;
    private AddProductToBranchUseCase useCase;
    @BeforeEach
    void setUp() {
        repository = mock(FranchiseRepository.class);
        useCase = new AddProductToBranchUseCase(repository);
    }

    @Test
    void shouldCreateAndSaveProductToFranchise() {
        Franchise franchise = new Franchise(UUID.randomUUID(), "Test Franchise");
        Branch branch = new Branch(UUID.randomUUID(), "Test Branch");
        franchise.addBranch(branch);

        when(repository.findById(anyString())).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Void> result = useCase.execute("franchiseId", branch.getId().toString(), "Laptop", 10);

        StepVerifier.create(result).verifyComplete();

        verify(repository).save(franchise);

        assertEquals(1, branch.getProducts().size());
        Product savedProduct = branch.getProducts().get(0);
        assertEquals("Laptop", savedProduct.getName());
        assertEquals(10, savedProduct.getStock());
    }
}
