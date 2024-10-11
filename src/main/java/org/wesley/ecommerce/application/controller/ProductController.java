package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.wesley.ecommerce.application.controller.dto.ProductRequestDTO;
import org.wesley.ecommerce.application.controller.dto.ProductResponseDTO;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.service.ProductService;

import java.util.ArrayList;
import java.util.List;

import static org.wesley.ecommerce.application.utility.CodeGenerate.randomCode;

@RestController
@RequestMapping("/product")
@Tag(name = "Product Controller", description = "RESTFul API for managing products.")
@RequiredArgsConstructor

public class ProductController {
    final private ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product", description = "Create a new product and return the created product's data")
    public ResponseEntity<ProductRequestDTO> createProduct(@RequestBody ProductRequestDTO product) {
        var productToCreate = productService.create(product.from(randomCode()));
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productToCreate.getId())
                .toUri();
        return ResponseEntity.created(location).body(product);
    }

    @GetMapping("/products")
    @Operation(summary = "Get all products", description = "Retrieve a list of all registered products")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        var products = productService.findAll();
        List<ProductResponseDTO> prods = new ArrayList<>(List.of());
        for (var product : products) {
            prods.add(ProductResponseDTO.fromDTO(product));
        }
        return ResponseEntity.ok(prods);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Retrieve a specific product based on its ID")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        var product = productService.findById(id);
        return ResponseEntity.ok(new ProductResponseDTO(product.getCode(), product.getName(), product.getDescription(), product.getPrice(), product.getStockQuantity(), product.getImageUrl(), product.getCategory()));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Remove product", description = "Remove product by ID")
    public ResponseEntity<ProductResponseDTO> deleteProduct(@PathVariable Long id) {
        var productToDelete = productService.findById(id);
        productService.delete(productToDelete);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a product", description = "Update the data of an existing user based on its ID")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductResponseDTO productResponseDTO) {
        var updatedProduct = productService.update(id, productResponseDTO.from(randomCode()));
        return ResponseEntity.ok(ProductResponseDTO.fromDTO(updatedProduct));
    }

    @GetMapping("/by-cart/{cartId}")
    public ResponseEntity<List<Product>> getProductsByCart(@PathVariable Long cartId) {
        var products = productService.findProductsByCart(cartId);
        return ResponseEntity.status(HttpStatus.FOUND).body(products);
    }
}
