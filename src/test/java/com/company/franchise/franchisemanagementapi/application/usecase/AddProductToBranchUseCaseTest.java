package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BranchNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.exception.BusinessException;
import com.company.franchise.franchisemanagementapi.domain.exception.InvalidStockException;
import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.domain.port.BranchRepository;
import com.company.franchise.franchisemanagementapi.domain.port.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddProductToBranchUseCaseTest {
    private BranchRepository branchRepository;
    private ProductRepository productRepository;
    private AddProductToBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        branchRepository = mock(BranchRepository.class);
        productRepository = mock(ProductRepository.class);
        useCase = new AddProductToBranchUseCase(branchRepository, productRepository);
    }

    @Test
    void shouldAddProductToBranchSuccessfully() {
        // GIVEN
        UUID fId = UUID.randomUUID();
        UUID bId = UUID.randomUUID();
        String pName = "Laptop";
        int stock = 10;
        Branch branch = new Branch(bId, "Test Branch");

        when(branchRepository.findById(fId, bId)).thenReturn(Mono.just(branch));
        when(productRepository.existsByNameInBranch(pName, bId)).thenReturn(Mono.just(false));
        when(productRepository.create(any(Product.class), eq(bId)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        Mono<Void> result = useCase.execute(fId, bId, pName, stock);

        StepVerifier.create(result).verifyComplete();

        verify(branchRepository).findById(fId, bId);
        verify(productRepository).existsByNameInBranch(pName, bId);
        verify(productRepository).create(any(Product.class), eq(bId));
    }

    @Test
    void shouldThrowExceptionWhenProductAlreadyExists() {
        UUID fId = UUID.randomUUID();
        UUID bId = UUID.randomUUID();
        String pName = "Laptop";
        Branch branch = new Branch(bId, "Test Branch");

        when(branchRepository.findById(fId, bId)).thenReturn(Mono.just(branch));
        when(productRepository.existsByNameInBranch(pName, bId)).thenReturn(Mono.just(true));

        Mono<Void> result = useCase.execute(fId, bId, pName, 10);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().contains("already exists in the branch"))
                .verify();

        verify(productRepository, never()).create(any(), any());
    }

    @Test
    void shouldReturnErrorWhenStockIsNegative() {
        Mono<Void> result = useCase.execute(UUID.randomUUID(), UUID.randomUUID(), "Product", -5);

        StepVerifier.create(result)
                .expectError(InvalidStockException.class)
                .verify();

        verify(branchRepository, never()).findById(any(), any());
    }

    @Test
    void shouldReturnErrorWhenBranchNotFound() {
        UUID fId = UUID.randomUUID();
        UUID bId = UUID.randomUUID();
        when(branchRepository.findById(fId, bId)).thenReturn(Mono.empty());

        Mono<Void> result = useCase.execute(fId, bId, "Laptop", 10);

        StepVerifier.create(result)
                .expectError(BranchNotFoundException.class)
                .verify();

        verify(productRepository, never()).existsByNameInBranch(any(), any());
    }
}