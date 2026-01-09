package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BusinessException;
import com.company.franchise.franchisemanagementapi.domain.exception.FranchiseNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateFranchiseNameUseCaseTest {

    private FranchiseRepository repository;
    private UpdateFranchiseNameUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(FranchiseRepository.class);
        useCase = new UpdateFranchiseNameUseCase(repository);
    }

    @Test
    void shouldUpdateFranchiseNameSuccessfully() {
        UUID id = UUID.randomUUID();
        String newName = "Nintendo Global";
        Franchise existingFranchise = new Franchise(id, "Nintendo");

        when(repository.existsByName(newName)).thenReturn(Mono.just(false));
        when(repository.findById(id)).thenReturn(Mono.just(existingFranchise));
        when(repository.update(any(Franchise.class))).thenReturn(Mono.empty());

        Mono<Void> result = useCase.execute(id, newName);

        StepVerifier.create(result).verifyComplete();

        verify(repository).update(argThat(f -> f.getName().equals(newName)));
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExists() {
        UUID id = UUID.randomUUID();
        String duplicatedName = "McDonalds";

        when(repository.existsByName(duplicatedName)).thenReturn(Mono.just(true));

        Mono<Void> result = useCase.execute(id, duplicatedName);

        StepVerifier.create(result)
                .expectErrorMatches(t -> t instanceof BusinessException &&
                        t.getMessage().contains("already another franchise with that name"))
                .verify();

        verify(repository, never()).findById(any());
        verify(repository, never()).update(any());
    }

    @Test
    void shouldThrowExceptionWhenFranchiseNotFound() {
        UUID id = UUID.randomUUID();
        String newName = "New Name";

        when(repository.existsByName(newName)).thenReturn(Mono.just(false));
        when(repository.findById(id)).thenReturn(Mono.empty());

        Mono<Void> result = useCase.execute(id, newName);

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(repository, never()).update(any());
    }
}