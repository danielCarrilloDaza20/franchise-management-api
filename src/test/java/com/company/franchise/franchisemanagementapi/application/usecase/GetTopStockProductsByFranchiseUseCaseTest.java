package com.company.franchise.franchisemanagementapi.application.usecase;

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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class GetTopStockProductsByFranchiseUseCaseTest {

    private FranchiseRepository repository;
    private GetTopStockProductsByFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(FranchiseRepository.class);
        useCase = new GetTopStockProductsByFranchiseUseCase(repository);
    }

    @Test
    @DisplayName("Debe retornar el producto con más stock por cada sucursal de la franquicia")
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

        when(repository.findById(franchiseId))
                .thenReturn(Mono.just(franchise));

        Mono<List<TopProductByBranch>> result = useCase.execute(franchiseId);

        StepVerifier.create(result)
                .assertNext(list -> {
                    assertNotNull(list);
                    assertEquals(2, list.size());

                    TopProductByBranch topA = list.stream()
                            .filter(t -> t.getBranchName().equals("Branch A"))
                            .findFirst().orElseThrow();
                    assertEquals("Product A2", topA.getProduct().getName());
                    assertEquals(30, topA.getProduct().getStock());

                    TopProductByBranch topB = list.stream()
                            .filter(t -> t.getBranchName().equals("Branch B"))
                            .findFirst().orElseThrow();
                    assertEquals("Product B2", topB.getProduct().getName());
                    assertEquals(20, topB.getProduct().getStock());
                })
                .verifyComplete();

        verify(repository, times(1)).findById(franchiseId);
    }

    @Test
    @DisplayName("Debe lanzar excepción si la franquicia no existe")
    void shouldFailWhenFranchiseDoesNotExist() {
        UUID franchiseId = UUID.randomUUID();
        when(repository.findById(franchiseId)).thenReturn(Mono.empty());

        Mono<List<TopProductByBranch>> result = useCase.execute(franchiseId);

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }
}

