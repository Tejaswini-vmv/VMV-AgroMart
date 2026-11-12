package com.vmv.agromart.controller;

import com.vmv.agromart.dto.ApiResponse;
import com.vmv.agromart.dto.ProductRequest;
import com.vmv.agromart.model.Product;
import com.vmv.agromart.security.UserPrincipal;
import com.vmv.agromart.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<Product>> list(@RequestParam(required = false) String category) {
        if (category != null && !category.isBlank()) {
            return ApiResponse.ok(productService.byCategory(Product.Category.valueOf(category.toUpperCase())));
        }
        return ApiResponse.ok(productService.listAvailable());
    }

    @GetMapping("/search")
    public ApiResponse<List<Product>> search(@RequestParam String q) {
        return ApiResponse.ok(productService.search(q));
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> get(@PathVariable Long id) {
        return ApiResponse.ok(productService.get(id));
    }

    @GetMapping("/mine")
    public ApiResponse<List<Product>> mine(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(productService.myProducts(principal.getUser()));
    }

    @PostMapping
    public ApiResponse<Product> create(@AuthenticationPrincipal UserPrincipal principal,
                                        @Valid @RequestBody ProductRequest request) {
        return ApiResponse.ok("Product created", productService.create(principal.getUser(), request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Product> update(@AuthenticationPrincipal UserPrincipal principal,
                                        @PathVariable Long id,
                                        @Valid @RequestBody ProductRequest request) {
        return ApiResponse.ok("Product updated", productService.update(principal.getUser(), id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        productService.delete(principal.getUser(), id);
        return ApiResponse.ok("Product removed", null);
    }
}
