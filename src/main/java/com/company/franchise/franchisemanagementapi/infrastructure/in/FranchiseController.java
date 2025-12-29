package com.company.franchise.franchisemanagementapi.infrastructure.in;

import com.company.franchise.franchisemanagementapi.application.usecase.*;
import com.company.franchise.franchisemanagementapi.infrastructure.in.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final AddBranchToFranchiseUseCase addBranchToFranchiseUseCase;
    private final AddProductToBranchUseCase addProductToBranchUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final GetTopStockProductsByFranchiseUseCase getTopStockProductsByFranchiseUseCase;

    @PostMapping
    public Mono<ResponseEntity<Void>> createFranchise(
            @RequestBody CreateFranchiseRequest request) {

        return createFranchiseUseCase.execute(request.getName())
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());
    }

    @PostMapping("/{franchiseId}/branches")
    public Mono<ResponseEntity<Void>> addBranch(
            @PathVariable String franchiseId,
            @RequestBody AddBranchRequest request) {

        return addBranchToFranchiseUseCase
                .execute(franchiseId, request.getName())
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());
    }

    @PostMapping("/{franchiseId}/branches/{branchId}/products")
    public Mono<ResponseEntity<Void>> addProduct(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @RequestBody AddProductRequest request) {

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
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId,
            @RequestBody UpdateProductStockRequest request) {

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
            @PathVariable String franchiseId) {

        return getTopStockProductsByFranchiseUseCase.execute(franchiseId)
                .map(list -> list.stream()
                        .map(TopProductByBranchResponse::fromDomain)
                        .toList()
                )
                .map(ResponseEntity::ok);
    }
}

