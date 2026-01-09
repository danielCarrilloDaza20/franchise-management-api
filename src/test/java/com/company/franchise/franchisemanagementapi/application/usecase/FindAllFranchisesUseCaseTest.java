package com.company.franchise.franchisemanagementapi.application.usecase;

import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.port.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FindAllFranchisesUseCaseTest {

    private FranchiseRepository repository;
    private FindAllFranchisesUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(FranchiseRepository.class);
        useCase = new FindAllFranchisesUseCase(repository);
    }

    @Test
    void shouldReturnPaginatedFranchises() {
        int page = 0;
        int size = 10;
        Franchise franchise = new Franchise(UUID.randomUUID(), "Test Franchise");

        when(repository.findAll(any(Pageable.class)))
                .thenReturn(Flux.just(franchise));

        Flux<Franchise> result = useCase.execute(page, size);

        StepVerifier.create(result)
                .expectNext(franchise)
                .verifyComplete();

        verify(repository).findAll(argThat(pageable ->
                pageable.getPageNumber() == page &&
                        pageable.getPageSize() == size));
    }
}
