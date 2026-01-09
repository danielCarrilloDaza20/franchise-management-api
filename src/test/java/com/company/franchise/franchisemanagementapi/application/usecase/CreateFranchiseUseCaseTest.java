package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BusinessException;
import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateFranchiseUseCaseTest {

    private FranchiseRepository repository;
    private CreateFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(FranchiseRepository.class);
        useCase = new CreateFranchiseUseCase(repository);
    }

    @Test
    void shouldCreateFranchiseSuccessfully() {
        String name = "Test Franchise";
        when(repository.existsByName(name)).thenReturn(Mono.just(false));
        when(repository.create(any(Franchise.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        Mono<Franchise> result = useCase.execute(name);

        StepVerifier.create(result)
                .assertNext(franchise -> {
                    assertEquals(name, franchise.getName());
                    assertNotNull(franchise.getId());
                })
                .verifyComplete();

        verify(repository).existsByName(name);
        verify(repository).create(any(Franchise.class));
    }

    @Test
    void shouldThrowExceptionWhenNameExists() {
        String name = "Existing Franchise";
        when(repository.existsByName(name)).thenReturn(Mono.just(true));

        Mono<Franchise> result = useCase.execute(name);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().contains("There is already a franchise"))
                .verify();

        verify(repository).existsByName(name);
        verify(repository, never()).create(any(Franchise.class));
    }
}