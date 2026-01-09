package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BranchNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.exception.BusinessException;
import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import com.company.franchise.franchisemanagementapi.domain.port.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.*;

class UpdateBranchNameUseCaseTest {

    private BranchRepository branchRepository;
    private UpdateBranchNameUseCase useCase;

    @BeforeEach
    void setUp() {
        branchRepository = mock(BranchRepository.class);
        useCase = new UpdateBranchNameUseCase(branchRepository);
    }

    @Test
    void shouldUpdateBranchNameSuccessfully() {
        UUID fId = UUID.randomUUID();
        UUID bId = UUID.randomUUID();
        String newName = "Sucursal Norte Renovada";
        Branch existingBranch = new Branch(bId, "Sucursal Norte");

        when(branchRepository.existsByNameInFranchise(newName, fId))
                .thenReturn(Mono.just(false));
        when(branchRepository.findById(fId, bId))
                .thenReturn(Mono.just(existingBranch));
        when(branchRepository.update(any(Branch.class), eq(fId)))
                .thenReturn(Mono.empty());

        Mono<Void> result = useCase.execute(fId, bId, newName);

        StepVerifier.create(result).verifyComplete();

        verify(branchRepository).update(argThat(b -> b.getName().equals(newName)), eq(fId));
    }

    @Test
    void shouldThrowExceptionWhenBranchNameAlreadyExists() {
        UUID fId = UUID.randomUUID();
        UUID bId = UUID.randomUUID();
        String duplicatedName = "Sucursal Centro";

        when(branchRepository.existsByNameInFranchise(duplicatedName, fId))
                .thenReturn(Mono.just(true));

        Mono<Void> result = useCase.execute(fId, bId, duplicatedName);

        StepVerifier.create(result)
                .expectErrorMatches(t -> t instanceof BusinessException &&
                        t.getMessage().contains("already has a branch with the name"))
                .verify();

        verify(branchRepository, never()).findById(any(), any());
        verify(branchRepository, never()).update(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenBranchNotFound() {
        UUID fId = UUID.randomUUID();
        UUID bId = UUID.randomUUID();
        String newName = "New Name";

        when(branchRepository.existsByNameInFranchise(newName, fId))
                .thenReturn(Mono.just(false));
        when(branchRepository.findById(fId, bId))
                .thenReturn(Mono.empty());

        Mono<Void> result = useCase.execute(fId, bId, newName);

        StepVerifier.create(result)
                .expectError(BranchNotFoundException.class)
                .verify();

        verify(branchRepository, never()).update(any(), any());
    }
}
