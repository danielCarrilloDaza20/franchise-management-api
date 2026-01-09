package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BranchNotFoundException;
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
    @DisplayName("Debe agregar un producto exitosamente a una sucursal existente")
    void shouldAddProductToBranchSuccessfully() {
        UUID franchiseId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        String productName = "Laptop";
        int stock = 10;

        Branch branch = new Branch(branchId, "Test Branch");

        when(branchRepository.findById(franchiseId, branchId))
                .thenReturn(Mono.just(branch));

        when(productRepository.create(any(Product.class), eq(branchId)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Void> result = useCase.execute(franchiseId, branchId, productName, stock);

        StepVerifier.create(result)
                .verifyComplete();

        verify(branchRepository, times(1)).findById(franchiseId, branchId);
        verify(productRepository, times(1)).create(any(Product.class), eq(branchId));
    }

    @Test
    @DisplayName("Debe lanzar error si el stock es negativo")
    void shouldReturnErrorWhenStockIsNegative() {
        UUID franchiseId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();

        Mono<Void> result = useCase.execute(franchiseId, branchId, "Invalid Product", -5);

        StepVerifier.create(result)
                .expectError(InvalidStockException.class)
                .verify();

        verify(branchRepository, never()).findById(any(), any());
        verify(productRepository, never()).create(any(), any());
    }

    @Test
    @DisplayName("Debe lanzar error si la sucursal no existe")
    void shouldReturnErrorWhenBranchNotFound() {
        UUID fId = UUID.randomUUID();
        UUID bId = UUID.randomUUID();
        when(branchRepository.findById(fId, bId)).thenReturn(Mono.empty());

        Mono<Void> result = useCase.execute(fId, bId, "Laptop", 10);

        StepVerifier.create(result)
                .expectError(BranchNotFoundException.class)
                .verify();
    }
}