package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BusinessException;
import com.company.franchise.franchisemanagementapi.domain.exception.ProductNotFoundException;
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

class RemoveProductFromBranchUseCaseTest {

    private ProductRepository productRepository;
    private BranchRepository branchRepository;
    private RemoveProductFromBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        branchRepository = mock(BranchRepository.class);
        useCase = new RemoveProductFromBranchUseCase(productRepository, branchRepository);
    }

    @Test
    void shouldRemoveProductFromBranchSuccessfully() {
        UUID fId = UUID.randomUUID();
        UUID bId = UUID.randomUUID();
        UUID pId = UUID.randomUUID();
        Product product = new Product(pId, "Laptop", 10);

        when(branchRepository.existsInFranchise(bId, fId)).thenReturn(Mono.just(true));
        when(productRepository.findById(bId, pId)).thenReturn(Mono.just(product));
        when(productRepository.deleteById(bId, pId)).thenReturn(Mono.empty());

        Mono<Void> result = useCase.execute(fId, bId, pId);

        StepVerifier.create(result).verifyComplete();

        verify(branchRepository).existsInFranchise(bId, fId);
        verify(productRepository).deleteById(bId, pId);
    }

    @Test
    void shouldFailWhenBranchDoesNotBelongToFranchise() {
        UUID fId = UUID.randomUUID();
        UUID bId = UUID.randomUUID();
        UUID pId = UUID.randomUUID();

        when(branchRepository.existsInFranchise(bId, fId)).thenReturn(Mono.just(false));

        Mono<Void> result = useCase.execute(fId, bId, pId);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().contains("does not belong to the indicated franchise"))
                .verify();

        verify(productRepository, never()).findById(any(), any());
        verify(productRepository, never()).deleteById(any(), any());
    }

    @Test
    void shouldReturnErrorWhenProductNotFound() {
        UUID fId = UUID.randomUUID();
        UUID bId = UUID.randomUUID();
        UUID pId = UUID.randomUUID();

        when(branchRepository.existsInFranchise(bId, fId)).thenReturn(Mono.just(true));
        when(productRepository.findById(bId, pId)).thenReturn(Mono.empty());

        Mono<Void> result = useCase.execute(fId, bId, pId);

        StepVerifier.create(result)
                .expectError(ProductNotFoundException.class)
                .verify();

        verify(productRepository, never()).deleteById(any(), any());
    }
}