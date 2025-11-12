package org.wesley.ecommerce.application.api.v1.controller;

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
import org.wesley.ecommerce.application.api.v1.controller.dto.request.*;
import org.wesley.ecommerce.application.api.v1.controller.dto.response.*;
import org.wesley.ecommerce.application.domain.model.ProductCategory;
import org.wesley.ecommerce.application.service.ProductCategoryService;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/v1/category")
@Tag(name = "Product Category Controller", description = "RESTFul API for managing products category.")
@Data
public class ProductCategoryController {
    final private ProductCategoryService productCategoryService;

    @PostMapping(consumes = "multipart/form-data")
    @Operation(
            summary = "Create a new product category",
            description = "Create a new product category"
    )
    public ResponseEntity<ProductCategory> createProduct(
            @RequestPart("data") @Valid CreateProductCategoryRequest product,
            @RequestPart("image") MultipartFile imageFile
    ) {
        var productToCreate = productCategoryService.create(product, imageFile);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productToCreate.getId())
                .toUri();
        return ResponseEntity.created(location).body(productToCreate);
    }

    @GetMapping
    @Operation(
            summary = "Get all products category",
            description = "Retrieve a list of all registered products category"
    )
    public ResponseEntity<ApiResponse<ProductCategoryResponse>> getAllProducts(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var categories = productCategoryService.findAll(name, page, pageSize);
        return getApiResponseResponseEntity(categories);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a product by ID",
            description = "Retrieve a specific product based on its ID"
    )
    public ResponseEntity<ProductCategoryResponse> getProductById(@PathVariable Long id) {
        var product = productCategoryService.findById(id);
        return ResponseEntity.ok(
                ProductCategoryResponse.fromDTO(product)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Remove product category",
            description = "Remove product category by ID"
    )
    public ResponseEntity<ProductCategoryResponse> deleteProductCategory(@PathVariable Long id) {
        var productCategoryToDelete = productCategoryService.findById(id);
        productCategoryService.delete(productCategoryToDelete);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @Operation(summary = "Update an existing product category", description = "Update an existing product category")
    public ResponseEntity<ProductCategory> updateProductCategory(
            @PathVariable Long id,
            @RequestPart("productCategoryData") @Valid UpdateProductCategoryRequest productRequest,
            @RequestPart(value = "image", required = false) MultipartFile newImage
    ) {
        var updatedProduct = productCategoryService.update(id, productRequest, newImage);
        return ResponseEntity.ok(updatedProduct);
    }

    @NotNull
    private ResponseEntity<ApiResponse<ProductCategoryResponse>> getApiResponseResponseEntity(Page<ProductCategory> products) {
        List<ProductCategoryResponse> prods = new ArrayList<>(List.of());
        for (var product : products) {
            prods.add(ProductCategoryResponse.fromDTO(product));
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
