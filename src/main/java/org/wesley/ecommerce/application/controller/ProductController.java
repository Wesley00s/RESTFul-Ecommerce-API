package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.wesley.ecommerce.application.controller.dto.request.UpdateProductRequest;
import org.wesley.ecommerce.application.controller.dto.response.ApiResponse;
import org.wesley.ecommerce.application.controller.dto.response.PaginationResponse;
import org.wesley.ecommerce.application.controller.dto.request.CreateProductRequest;
import org.wesley.ecommerce.application.controller.dto.response.ProductResponse;
import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import org.wesley.ecommerce.application.domain.enumeration.ProductSortBy;
import org.wesley.ecommerce.application.domain.enumeration.SortDirection;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.service.ProductService;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/product")
@Tag(name = "Product Controller", description = "RESTFul API for managing products.")
@Data

public class ProductController {
    final private ProductService productService;

    @PostMapping(consumes = "multipart/form-data")
    @Operation(
            summary = "Create a new product",
            description = "Create a new product and return the created product's data"
    )
    public ResponseEntity<Product> createProduct(
            @RequestPart("productData") @Valid CreateProductRequest product,
            @RequestPart("coverImage") MultipartFile coverImageFile,
            @RequestPart(value = "otherImages", required = false) List<MultipartFile> otherImageFiles
    ) {
        var productToCreate = productService.create(product, coverImageFile, otherImageFiles);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productToCreate.getId())
                .toUri();
        return ResponseEntity.created(location).body(productToCreate);
    }

    @GetMapping
    @Operation(
            summary = "Get all products",
            description = "Retrieve a list of all registered products"
    )
    public ResponseEntity<ApiResponse<ProductResponse>> getAllProducts(
            @RequestParam(name = "sortBy", required = false) ProductSortBy sortBy,
            @RequestParam(name = "sortDirection", required = false) SortDirection sortDirection,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var products = productService.findAll(sortBy, sortDirection, name, page, pageSize);
        return getApiResponseResponseEntity(products);
    }

    @GetMapping("/category/{category}")
    @Operation(
            summary = "Get products by category",
            description = "Retrieve a list of products filtered by category"
    )
    public ResponseEntity<ApiResponse<ProductResponse>> getProductsByCategory(
            @PathVariable ProductCategory category,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var products = productService.findProductsByCategory(category, page, pageSize);
        return getApiResponseResponseEntity(products);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a product by ID",
            description = "Retrieve a specific product based on its ID"
    )
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        var product = productService.findById(id);
        return ResponseEntity.ok(
                ProductResponse.fromDTO(product)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Remove product",
            description = "Remove product by ID"
    )
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long id) {
        var productToDelete = productService.findById(id);
        productService.delete(productToDelete);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @Operation(summary = "Update an existing product")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestPart("productData") @Valid UpdateProductRequest productRequest,
            @RequestPart(value = "coverImage", required = false) MultipartFile newCoverImage,
            @RequestPart(value = "newImages", required = false) List<MultipartFile> newImageFiles
    ) {
        Product updatedProduct = productService.update(id, productRequest, newCoverImage, newImageFiles);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/cart/{cartId}")
    @Operation(
            summary = "Get product from a cart",
            description = "Retrieve a list of products from a cart"
    )
    public ResponseEntity<List<Product>> getProductsByCart(@PathVariable Long cartId) {
        var products = productService.findProductsByCart(cartId);
        return ResponseEntity.ok(products);
    }

    @NotNull
    private ResponseEntity<ApiResponse<ProductResponse>> getApiResponseResponseEntity(Page<Product> products) {
        List<ProductResponse> prods = new ArrayList<>(List.of());
        for (var product : products) {
            prods.add(ProductResponse.fromDTO(product));
        }
        var pageResponse = new ApiResponse<>(
                prods,
                new PaginationResponse(
                        products.getNumber(),
                        products.getSize(),
                        products.getTotalElements(),
                        products.getTotalPages()
                )
        );
        return ResponseEntity.ok(pageResponse);
    }
}
