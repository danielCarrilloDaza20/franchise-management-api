package com.company.franchise.franchisemanagementapi.application.usecase;

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
    @DisplayName("Debe invocar el método create del repositorio al generar una nueva franquicia")
    void shouldCreateFranchiseSuccessfully() {
        String franchiseName = "Test Franchise";

        when(repository.create(any(Franchise.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Franchise> result = useCase.execute(franchiseName);

        StepVerifier.create(result)
                .assertNext(franchise -> {
                    assertNotNull(franchise);
                    assertEquals(franchiseName, franchise.getName());
                    assertNotNull(franchise.getId()); // El dominio ya le puso ID
                })
                .verifyComplete();

        verify(repository, times(1)).create(any(Franchise.class));

    }
}

