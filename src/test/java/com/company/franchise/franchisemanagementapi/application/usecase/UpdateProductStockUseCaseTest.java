package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BranchNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.exception.InvalidStockException;
import com.company.franchise.franchisemanagementapi.domain.exception.ProductNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.domain.port.BranchRepository;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import com.company.franchise.franchisemanagementapi.domain.port.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateProductStockUseCaseTest {

    private BranchRepository branchRepository;
    private ProductRepository productRepository;
    private FranchiseRepository franchiseRepository;
    private UpdateProductStockUseCase useCase;

    @BeforeEach
    void setUp() {
        branchRepository = mock(BranchRepository.class);
        productRepository = mock(ProductRepository.class);
        franchiseRepository = mock(FranchiseRepository.class);

        useCase = new UpdateProductStockUseCase(
                franchiseRepository,
                branchRepository,
                productRepository
        );
    }

    @Test
    void shouldUpdateProductStockSuccessfully() {
        UUID fId = UUID.randomUUID();
        UUID bId = UUID.randomUUID();
        UUID pId = UUID.randomUUID();
        int newStock = 20;

        Branch branch = new Branch(bId, "Branch 1");
        Product product = new Product(pId, "Laptop", 5);

        when(branchRepository.findById(fId, bId)).thenReturn(Mono.just(branch));
        when(productRepository.findById(bId, pId)).thenReturn(Mono.just(product));
        when(productRepository.update(any(Product.class), eq(bId)))
                .thenReturn(Mono.empty());

        Mono<Void> result = useCase.execute(fId, bId, pId, newStock);

        StepVerifier.create(result).verifyComplete();

        verify(productRepository).update(argThat(p -> p.getStock() == newStock), eq(bId));
    }

    @Test
    void shouldNotUpdateWhenStockIsSame() {
        UUID fId = UUID.randomUUID();
        UUID bId = UUID.randomUUID();
        UUID pId = UUID.randomUUID();
        int sameStock = 10;

        Product product = new Product(pId, "Laptop", sameStock); // Ya tiene 10

        when(branchRepository.findById(fId, bId)).thenReturn(Mono.just(new Branch(bId, "B1")));
        when(productRepository.findById(bId, pId)).thenReturn(Mono.just(product));

        Mono<Void> result = useCase.execute(fId, bId, pId, sameStock);

        StepVerifier.create(result).verifyComplete();

        verify(productRepository, never()).update(any(), any());
    }

    @Test
    void shouldFailWhenStockIsNegative() {
        Mono<Void> result = useCase.execute(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), -10);

        StepVerifier.create(result)
                .expectError(InvalidStockException.class)
                .verify();

        verifyNoInteractions(branchRepository, productRepository);
    }

    @Test
    void shouldFailWhenBranchNotFound() {
        UUID fId = UUID.randomUUID();
        UUID bId = UUID.randomUUID();

        when(branchRepository.findById(fId, bId)).thenReturn(Mono.empty());

        Mono<Void> result = useCase.execute(fId, bId, UUID.randomUUID(), 10);

        StepVerifier.create(result)
                .expectErrorMatches(t -> t instanceof BranchNotFoundException &&
                        t.getMessage().contains("Branch not found in the franchise"))
                .verify();
    }
}