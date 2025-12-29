package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AddBranchToFranchiseUseCaseTest {
    private FranchiseRepository repository;
    private AddBranchToFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(FranchiseRepository.class);
        useCase = new AddBranchToFranchiseUseCase(repository);
    }

    @Test
    void shouldCreateAndSaveBranchToFranchise() {
        Franchise franchise = new Franchise(UUID.randomUUID(), "Test Franchise");

        when(repository.findById(anyString()))
                .thenReturn(Mono.just(franchise));

        when(repository.save(any(Franchise.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Void> result = useCase.execute("123", "New Branch");

        StepVerifier.create(result)
                .verifyComplete();

        verify(repository).save(franchise);
        assertEquals(1, franchise.getBranches().size());
        assertEquals("New Branch", franchise.getBranches().get(0).getName());



    }

}
