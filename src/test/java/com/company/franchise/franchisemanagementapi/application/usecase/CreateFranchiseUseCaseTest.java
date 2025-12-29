package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
    void shouldCreateAndSaveFranchise() {
        String franchiseName = "Test Franchise";

        when(repository.save(any(Franchise.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Franchise> result = useCase.execute(franchiseName);

        StepVerifier.create(result)
                .assertNext(franchise -> {
                    assertNotNull(franchise);
                    assertNotNull(franchise.getId());
                })
                .verifyComplete();

        ArgumentCaptor<Franchise> captor = ArgumentCaptor.forClass(Franchise.class);
        verify(repository, times(1)).save(captor.capture());

        Franchise savedFranchise = captor.getValue();
        assertNotNull(savedFranchise.getId());
    }
}

