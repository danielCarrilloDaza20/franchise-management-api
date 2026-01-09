package com.company.franchise.franchisemanagementapi.application.usecase;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @DisplayName("Debe agregar una sucursal exitosamente a una franquicia existente")
    void shouldCreateAndSaveBranchToFranchise() {
        UUID franchiseId = UUID.randomUUID();
        String branchName = "New Branch";
        Franchise franchise = new Franchise(franchiseId, "Test Franchise");

        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchise));

        when(branchRepository.create(any(Branch.class), eq(franchiseId)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Void> result = useCase.execute(franchiseId, branchName);

        StepVerifier.create(result)
                .verifyComplete();

        verify(franchiseRepository, times(1)).findById(franchiseId);
        verify(branchRepository, times(1)).create(any(Branch.class), eq(franchiseId));

        assertEquals(1, franchise.getBranches().size());
        assertEquals(branchName, franchise.getBranches().get(0).getName());
    }

    @Test
    @DisplayName("Debe lanzar error si la franquicia no existe")
    void shouldReturnErrorWhenFranchiseNotFound() {
        // GIVEN
        UUID franchiseId = UUID.randomUUID();
        when(franchiseRepository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // WHEN
        Mono<Void> result = useCase.execute(franchiseId, "Any Branch");

        // THEN
        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(branchRepository, never()).create(any(), any());
    }
}