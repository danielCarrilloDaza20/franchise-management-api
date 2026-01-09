package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BusinessException;
import com.company.franchise.franchisemanagementapi.domain.exception.ProductNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.domain.port.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.*;

class UpdateProductNameUseCaseTest {

    private ProductRepository productRepository;
    private UpdateProductNameUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        useCase = new UpdateProductNameUseCase(productRepository);
    }

    @Test
    void shouldUpdateProductNameSuccessfully() {
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        String newName = "Teclado Mecánico RGB";
        Product existingProduct = new Product(productId, "Teclado", 15);

        when(productRepository.existsByNameInBranch(newName, branchId))
                .thenReturn(Mono.just(false));
        when(productRepository.findById(branchId, productId))
                .thenReturn(Mono.just(existingProduct));
        when(productRepository.update(any(Product.class), eq(branchId)))
                .thenReturn(Mono.empty());

        Mono<Void> result = useCase.execute(branchId, productId, newName);

        StepVerifier.create(result).verifyComplete();

        verify(productRepository).update(argThat(p -> p.getName().equals(newName)), eq(branchId));
    }

    @Test
    void shouldThrowExceptionWhenProductNameExistsInBranch() {
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        String duplicatedName = "Mouse Gamer";

        when(productRepository.existsByNameInBranch(duplicatedName, branchId))
                .thenReturn(Mono.just(true));

        Mono<Void> result = useCase.execute(branchId, productId, duplicatedName);

        StepVerifier.create(result)
                .expectErrorMatches(t -> t instanceof BusinessException &&
                        t.getMessage().contains("already a product with that name in this branch"))
                .verify();

        verify(productRepository, never()).findById(any(), any());
        verify(productRepository, never()).update(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        String newName = "New Name";

        when(productRepository.existsByNameInBranch(newName, branchId))
                .thenReturn(Mono.just(false));
        when(productRepository.findById(branchId, productId))
                .thenReturn(Mono.empty());

        Mono<Void> result = useCase.execute(branchId, productId, newName);

        StepVerifier.create(result)
                .expectError(ProductNotFoundException.class)
                .verify();

        verify(productRepository, never()).update(any(), any());
    }
}
