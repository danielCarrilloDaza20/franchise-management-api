package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.ProductNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.domain.port.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RemoveProductFromBranchUseCaseTest {

    private ProductRepository productRepository;
    private RemoveProductFromBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        useCase = new RemoveProductFromBranchUseCase(productRepository);
    }

    @Test
    @DisplayName("Debe eliminar un producto exitosamente si existe en la sucursal")
    void shouldRemoveProductFromBranchSuccessfully() {
        UUID franchiseId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Product product = new Product(productId, "Laptop", 10);

        when(productRepository.findById(branchId, productId))
                .thenReturn(Mono.just(product));

        when(productRepository.deleteById(branchId, productId))
                .thenReturn(Mono.empty()); // delete habitualmente retorna Mono<Void>

        Mono<Void> result = useCase.execute(franchiseId, branchId, productId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository, times(1)).findById(branchId, productId);
        verify(productRepository, times(1)).deleteById(branchId, productId);
    }

    @Test
    @DisplayName("Debe lanzar error si el producto no existe en la sucursal")
    void shouldReturnErrorWhenProductNotFound() {
        UUID fId = UUID.randomUUID();
        UUID bId = UUID.randomUUID();
        UUID pId = UUID.randomUUID();

        when(productRepository.findById(bId, pId)).thenReturn(Mono.empty());

        Mono<Void> result = useCase.execute(fId, bId, pId);

        StepVerifier.create(result)
                .expectError(ProductNotFoundException.class)
                .verify();

        verify(productRepository, never()).deleteById(any(), any());
    }
}