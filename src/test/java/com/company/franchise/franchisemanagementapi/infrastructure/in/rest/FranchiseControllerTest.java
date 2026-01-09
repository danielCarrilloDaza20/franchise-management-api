package com.company.franchise.franchisemanagementapi.infrastructure.in.rest;

import com.company.franchise.franchisemanagementapi.application.usecase.*;
import com.company.franchise.franchisemanagementapi.domain.model.Franchise;
import com.company.franchise.franchisemanagementapi.domain.model.Product;
import com.company.franchise.franchisemanagementapi.domain.model.TopProductByBranch;
import com.company.franchise.franchisemanagementapi.infrastructure.in.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
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
    @MockitoBean
    private RemoveProductFromBranchUseCase removeProductFromBranchUseCase;
    @MockitoBean
    private UpdateFranchiseNameUseCase updateFranchiseNameUseCase;
    @MockitoBean
    private UpdateBranchNameUseCase updateBranchNameUseCase;
    @MockitoBean
    private UpdateProductNameUseCase updateProductNameUseCase;

    private final UUID fId = UUID.randomUUID();
    private final UUID bId = UUID.randomUUID();
    private final UUID pId = UUID.randomUUID();

    @Test
    void shouldCreateFranchise() {
        CreateFranchiseRequest request = new CreateFranchiseRequest("Test Franchise");
        Franchise franchiseResponse = new Franchise(fId, "Test Franchise");

        when(createFranchiseUseCase.execute("Test Franchise"))
                .thenReturn(Mono.just(franchiseResponse));

        webTestClient.post()
                .uri("/franchises")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void shouldAddBranchToFranchise() {
        AddBranchRequest request = new AddBranchRequest("Main Branch");

        when(addBranchToFranchiseUseCase.execute(eq(fId), eq("Main Branch")))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/franchises/{id}/branches", fId.toString())
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();

        verify(addBranchToFranchiseUseCase).execute(fId, "Main Branch");
    }

    @Test
    void shouldAddProductToBranch() {
        AddProductRequest request = new AddProductRequest("Laptop", 10);

        when(addProductToBranchUseCase.execute(eq(fId), eq(bId), eq("Laptop"), eq(10)))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/franchises/{fId}/branches/{bId}/products", fId.toString(), bId.toString())
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();

        verify(addProductToBranchUseCase).execute(fId, bId, "Laptop", 10);
    }

    @Test
    void shouldUpdateProductStock() {
        UpdateProductStockRequest request = new UpdateProductStockRequest(50);

        when(updateProductStockUseCase.execute(any(UUID.class), any(UUID.class), any(UUID.class), anyInt()))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/franchises/{fId}/branches/{bId}/products/{pId}/stock",
                        fId, bId, pId) // No hace falta .toString(), WebTestClient lo maneja
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        verify(updateProductStockUseCase).execute(eq(fId), eq(bId), eq(pId), eq(50));
    }

    @Test
    void shouldReturnTopStockProductsByBranch() {
        Product productA = new Product(UUID.randomUUID(), "Laptop", 30);
        TopProductByBranch topA = new TopProductByBranch(UUID.randomUUID().toString(), "Branch A", productA);

        when(getTopStockProductsByFranchiseUseCase.execute(fId))
                .thenReturn(Mono.just(List.of(topA)));

        webTestClient.get()
                .uri("/franchises/{id}/products/top-stock", fId.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].branchName").isEqualTo("Branch A")
                .jsonPath("$[0].productName").isEqualTo("Laptop")
                .jsonPath("$[0].stock").isEqualTo(30);
    }

    @Test
    void shouldRemoveProductFromBranch() {
        when(removeProductFromBranchUseCase.execute(eq(fId), eq(bId), eq(pId)))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/franchises/{fId}/branches/{bId}/products/{pId}",
                        fId.toString(), bId.toString(), pId.toString())
                .exchange()
                .expectStatus().isNoContent();

        verify(removeProductFromBranchUseCase).execute(fId, bId, pId);
    }

    @Test
    void shouldUpdateFranchiseName() {
        UpdateFranchiseNameRequest request = new UpdateFranchiseNameRequest("New Franchise Name");

        when(updateFranchiseNameUseCase.execute(eq(fId), eq("New Franchise Name")))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/franchises/{id}", fId.toString())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        verify(updateFranchiseNameUseCase).execute(fId, "New Franchise Name");
    }

    @Test
    void shouldUpdateBranchName() {
        UpdateBranchNameRequest request = new UpdateBranchNameRequest("New Branch Name");

        when(updateBranchNameUseCase.execute(eq(fId), eq(bId), eq("New Branch Name")))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/franchises/{fId}/branches/{bId}", fId.toString(), bId.toString())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        verify(updateBranchNameUseCase).execute(fId, bId, "New Branch Name");
    }

    @Test
    void shouldUpdateProductName() {
        UpdateProductNameRequest request = new UpdateProductNameRequest("New Product Name");

        when(updateProductNameUseCase.execute(eq(bId), eq(pId), eq("New Product Name")))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/franchises/{fId}/branches/{bId}/products/{pId}",
                        fId.toString(), bId.toString(), pId.toString())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        verify(updateProductNameUseCase).execute(bId, pId, "New Product Name");
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() {
        UpdateProductNameRequest request = new UpdateProductNameRequest("");

        webTestClient.put()
                .uri("/franchises/{fId}/branches/{bId}/products/{pId}",
                        fId.toString(), bId.toString(), pId.toString())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }
}