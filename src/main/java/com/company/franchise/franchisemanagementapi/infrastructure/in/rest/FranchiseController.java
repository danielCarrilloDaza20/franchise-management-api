package com.company.franchise.franchisemanagementapi.infrastructure.in.rest;

import com.company.franchise.franchisemanagementapi.application.usecase.*;
import com.company.franchise.franchisemanagementapi.infrastructure.in.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final AddBranchToFranchiseUseCase addBranchToFranchiseUseCase;
    private final AddProductToBranchUseCase addProductToBranchUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final GetTopStockProductsByFranchiseUseCase getTopStockProductsByFranchiseUseCase;
    private final RemoveProductFromBranchUseCase removeProductFromBranchUseCase;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;
    private final UpdateBranchNameUseCase updateBranchNameUseCase;
    private final UpdateProductNameUseCase updateProductNameUseCase;
    private final FindAllFranchisesUseCase findAllFranchisesUseCase;

    @PostMapping
    public Mono<ResponseEntity<Void>> createFranchise(
            @Valid @RequestBody CreateFranchiseRequest request) {

        return createFranchiseUseCase.execute(request.getName())
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());
    }

    @PostMapping("/{franchiseId}/branches")
    public Mono<ResponseEntity<Void>> addBranch(
            @PathVariable UUID franchiseId,
            @Valid @RequestBody AddBranchRequest request) {

        return addBranchToFranchiseUseCase
                .execute(franchiseId, request.getName())
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());
    }

    @PostMapping("/{franchiseId}/branches/{branchId}/products")
    public Mono<ResponseEntity<Void>> addProduct(
            @PathVariable UUID franchiseId,
            @PathVariable UUID branchId,
            @Valid @RequestBody AddProductRequest request) {

        return addProductToBranchUseCase.execute(
                        franchiseId,
                        branchId,
                        request.getName(),
                        request.getStock()
                )
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());
    }

    @PutMapping("/{franchiseId}/branches/{branchId}/products/{productId}/stock")
    public Mono<ResponseEntity<Void>> updateProductStock(
            @PathVariable UUID franchiseId,
            @PathVariable UUID branchId,
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateProductStockRequest request) {

        return updateProductStockUseCase.execute(
                        franchiseId,
                        branchId,
                        productId,
                        request.getStock()
                )
                .thenReturn(ResponseEntity.ok().build());
    }

    @GetMapping("/{franchiseId}/products/top-stock")
    public Mono<ResponseEntity<List<TopProductByBranchResponse>>> getTopStockProducts(
            @PathVariable UUID franchiseId) {

        return getTopStockProductsByFranchiseUseCase.execute(franchiseId)
                .map(list -> list.stream()
                        .map(TopProductByBranchResponse::fromDomain)
                        .toList()
                )
                .map(ResponseEntity::ok);
    }

    @DeleteMapping(
            "/{franchiseId}/branches/{branchId}/products/{productId}"
    )
    public Mono<ResponseEntity<Void>> removeProduct(
            @PathVariable UUID franchiseId,
            @PathVariable UUID branchId,
            @PathVariable UUID productId
    ) {
        return removeProductFromBranchUseCase.execute(
                        franchiseId,
                        branchId,
                        productId
                )
                .thenReturn(ResponseEntity.noContent().build());
    }

    @PutMapping("/{franchiseId}")
    public Mono<ResponseEntity<Void>> updateFranchiseName(
            @PathVariable UUID franchiseId,
            @Valid @RequestBody UpdateFranchiseNameRequest request) {

        return updateFranchiseNameUseCase.execute(franchiseId, request.getName())
                .thenReturn(ResponseEntity.ok().build());
    }

    @PutMapping("/{franchiseId}/branches/{branchId}")
    public Mono<ResponseEntity<Void>> updateBranchName(
            @PathVariable UUID franchiseId,
            @PathVariable UUID branchId,
            @Valid @RequestBody UpdateBranchNameRequest request) {

        return updateBranchNameUseCase.execute(franchiseId, branchId, request.getName())
                .thenReturn(ResponseEntity.ok().build());
    }

    @PutMapping("/{franchiseId}/branches/{branchId}/products/{productId}")
    public Mono<ResponseEntity<Void>> updateBranchName(
            @PathVariable UUID franchiseId,
            @PathVariable UUID branchId,
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateProductNameRequest request) {

        return updateProductNameUseCase.execute(branchId, productId, request.getName())
                .thenReturn(ResponseEntity.ok().build());
    }

    @GetMapping
    public Flux<FranchiseResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return findAllFranchisesUseCase.execute(page, size)
                .map(FranchiseResponse::fromDomain);
    }

}

