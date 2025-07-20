package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.wesley.ecommerce.application.controller.dto.ApiResponse;
import org.wesley.ecommerce.application.controller.dto.PaginationResponse;
import org.wesley.ecommerce.application.controller.dto.ProductRequestDTO;
import org.wesley.ecommerce.application.controller.dto.ProductResponseDTO;
import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.service.ProductService;

import java.util.ArrayList;
import java.util.List;

import static org.wesley.ecommerce.application.utility.CodeGenerate.randomCode;

@RestController
@RequestMapping("/product")
@Tag(name = "Product Controller", description = "RESTFul API for managing products.")
@Data

public class ProductController {
    final private ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product", description = "Create a new product and return the created product's data")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDTO product) {
        var productToCreate = productService.create(product.from(randomCode()));
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productToCreate.getId())
                .toUri();
        return ResponseEntity.created(location).body(productToCreate);
    }

    @PostMapping("/list")
    @Operation(summary = "Save an list of products", description = "Create an list of products")
    public ResponseEntity<ArrayList<Product>> createProducts(@RequestBody List<ProductRequestDTO> products) {
        var prods = new ArrayList<Product>();
        products.forEach(product -> prods.add(productService.create(product.from(randomCode()))));
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();
        return ResponseEntity.created(location).body(prods);
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve a list of all registered products")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        var products = productService.findAll();
        List<ProductResponseDTO> prods = new ArrayList<>(List.of());
        for (var product : products) {
            prods.add(ProductResponseDTO.fromDTO(product));
        }
        return ResponseEntity.ok(prods);
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var products = productService.findAll(page, pageSize);
        return getApiResponseResponseEntity(products);
    }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Retrieve a specific product based on its ID")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        var product = productService.findById(id);
        return ResponseEntity.ok(
                new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getCode(),
                product.getDescription(),
                product.getStock(),
                product.getCategory(),
                product.getPrice()
        ));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove product", description = "Remove product by ID")
    public ResponseEntity<ProductResponseDTO> deleteProduct(@PathVariable Long id) {
        var productToDelete = productService.findById(id);
        productService.delete(productToDelete);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Update the data of an existing user based on its ID")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductResponseDTO productResponseDTO) {
        var updatedProduct = productService.update(id, productResponseDTO.from(randomCode()));
        return ResponseEntity.ok(ProductResponseDTO.fromDTO(updatedProduct));
    }

    @GetMapping("/cart/{cartId}")
    @Operation(summary = "Get product from a cart", description = "Retrieve a list of products from a cart")
    public ResponseEntity<List<Product>> getProductsByCart(@PathVariable Long cartId) {
        var products = productService.findProductsByCart(cartId);
        return ResponseEntity.status(HttpStatus.FOUND).body(products);
    }

    @NotNull
    private ResponseEntity<ApiResponse<ProductResponseDTO>> getApiResponseResponseEntity(Page<Product> products) {
        List<ProductResponseDTO> prods = new ArrayList<>(List.of());
        for (var product : products) {
            prods.add(ProductResponseDTO.fromDTO(product));
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
