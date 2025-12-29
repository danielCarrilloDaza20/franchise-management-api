package com.company.franchise.franchisemanagementapi.infrastructure;

import com.company.franchise.franchisemanagementapi.application.usecase.*;
import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.domain.model.TopProductByBranch;
import com.company.franchise.franchisemanagementapi.infrastructure.in.FranchiseController;
import com.company.franchise.franchisemanagementapi.infrastructure.in.dto.AddBranchRequest;
import com.company.franchise.franchisemanagementapi.infrastructure.in.dto.AddProductRequest;
import com.company.franchise.franchisemanagementapi.infrastructure.in.dto.CreateFranchiseRequest;
import com.company.franchise.franchisemanagementapi.infrastructure.in.dto.UpdateProductStockRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = FranchiseController.class)
class FranchiseControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreateFranchiseUseCase createFranchiseUseCase;

    @MockitoBean
    private AddBranchToFranchiseUseCase addBranchToFranchiseUseCase;

    @MockitoBean
    private AddProductToBranchUseCase addProductToBranchUseCase;

    @MockitoBean
    private UpdateProductStockUseCase updateProductStockUseCase;

    @MockitoBean
    private GetTopStockProductsByFranchiseUseCase getTopStockProductsByFranchiseUseCase;

    @Test
    void shouldCreateFranchise() {
        CreateFranchiseRequest request =
                new CreateFranchiseRequest("Test Franchise");

        when(createFranchiseUseCase.execute(anyString()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/franchises")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();

        verify(createFranchiseUseCase).execute("Test Franchise");
    }

    @Test
    void shouldAddBranchToFranchise() {
        AddBranchRequest request = new AddBranchRequest("Main Branch");

        when(addBranchToFranchiseUseCase.execute(anyString(), anyString()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/franchises/{id}/branches", "123")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();

        verify(addBranchToFranchiseUseCase)
                .execute("123", "Main Branch");
    }

    @Test
    void shouldAddProductToBranch() {
        AddProductRequest request = new AddProductRequest("Laptop", 10);

        when(addProductToBranchUseCase.execute(
                anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/franchises/{fId}/branches/{bId}/products", "f1", "b1")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();

        verify(addProductToBranchUseCase)
                .execute("f1", "b1", "Laptop", 10);
    }

    @Test
    void shouldUpdateProductStock() {
        UpdateProductStockRequest request =
                new UpdateProductStockRequest(50);

        when(updateProductStockUseCase.execute(
                anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/franchises/{fId}/branches/{bId}/products/{pId}/stock",
                        "f1", "b1", "p1")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        verify(updateProductStockUseCase)
                .execute("f1", "b1", "p1", 50);
    }

    @Test
    void shouldReturnTopStockProductsByBranch() {
        Product productA = new Product(UUID.randomUUID(), "Laptop", 30);
        Product productB = new Product(UUID.randomUUID(), "Mouse", 20);

        TopProductByBranch topA = new TopProductByBranch(
                "branch-1",
                "Branch A",
                productA
        );

        TopProductByBranch topB = new TopProductByBranch(
                "branch-2",
                "Branch B",
                productB
        );

        List<TopProductByBranch> response = List.of(topA, topB);

        when(getTopStockProductsByFranchiseUseCase.execute(anyString()))
                .thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/franchises/{id}/products/top-stock", "123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].branchName").isEqualTo("Branch A")
                .jsonPath("$[0].productName").isEqualTo("Laptop")
                .jsonPath("$[0].stock").isEqualTo(30)
                .jsonPath("$[1].branchName").isEqualTo("Branch B")
                .jsonPath("$[1].productName").isEqualTo("Mouse")
                .jsonPath("$[1].stock").isEqualTo(20);
    }

}

