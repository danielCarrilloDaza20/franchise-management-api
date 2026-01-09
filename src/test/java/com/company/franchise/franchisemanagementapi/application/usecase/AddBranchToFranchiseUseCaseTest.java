package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.exception.BusinessException;
import com.company.franchise.franchisemanagementapi.domain.exception.FranchiseNotFoundException;
import com.company.franchise.franchisemanagementapi.domain.model.Branch;
import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.port.BranchRepository;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.*;

class AddBranchToFranchiseUseCaseTest {
    private FranchiseRepository franchiseRepository;
    private BranchRepository branchRepository;
    private AddBranchToFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        franchiseRepository = mock(FranchiseRepository.class);
        branchRepository = mock(BranchRepository.class);
        useCase = new AddBranchToFranchiseUseCase(franchiseRepository, branchRepository);
    }

    @Test
    void shouldCreateAndSaveBranchSuccessfully() {
        UUID franchiseId = UUID.randomUUID();
        String branchName = "New Branch";
        Franchise franchise = new Franchise(franchiseId, "Test Franchise");

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(branchRepository.existsByNameInFranchise(branchName, franchiseId)).thenReturn(Mono.just(false));
        when(branchRepository.create(any(Branch.class), eq(franchiseId)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        Mono<Void> result = useCase.execute(franchiseId, branchName);

        StepVerifier.create(result).verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
        verify(branchRepository).existsByNameInFranchise(branchName, franchiseId);
        verify(branchRepository).create(any(Branch.class), eq(franchiseId));
    }

    @Test
    void shouldThrowExceptionWhenBranchNameExists() {
        UUID franchiseId = UUID.randomUUID();
        String branchName = "Existing Branch";
        Franchise franchise = new Franchise(franchiseId, "Test Franchise");

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(branchRepository.existsByNameInFranchise(branchName, franchiseId)).thenReturn(Mono.just(true));

        Mono<Void> result = useCase.execute(franchiseId, branchName);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().contains("already has a branch called"))
                .verify();

        verify(branchRepository).existsByNameInFranchise(branchName, franchiseId);
        verify(branchRepository, never()).create(any(), any());
    }

    @Test
    void shouldReturnErrorWhenFranchiseNotFound() {
        UUID franchiseId = UUID.randomUUID();
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.empty());

        Mono<Void> result = useCase.execute(franchiseId, "Any Branch");

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(branchRepository, never()).existsByNameInFranchise(any(), any());
        verify(branchRepository, never()).create(any(), any());
    }
}