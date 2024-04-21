package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.wesley.ecommerce.application.controller.dto.ProductDTO;
import org.wesley.ecommerce.application.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/ecommerce/product")
@Tag(name = "Product Controller", description = "RESTFul API for managing products.")
public class ProductController {
    final private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Create a new product and return the created product's data")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product) {
        var productToCreate = productService.create(product.from());
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productToCreate.getProductId())
                .toUri();
        return ResponseEntity.created(location).body(product);
    }

    @GetMapping("/products")
    @Operation(summary = "Get all products", description = "Retrieve a list of all registered products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        var products = productService.findAll();
        var productList = products.stream()
                .map(product -> new ProductDTO(
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStockQuantity(),
                        product.getImageUrl(),
                        product.getCategoryName(),
                        product.getCategoryDescription()
                )).toList();
        return ResponseEntity.ofNullable(productList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Retrieve a specific product based on its ID")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        var product = productService.findById(id);
        return ResponseEntity.ok(new ProductDTO(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategoryName(),
                product.getCategoryDescription()
        ));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Remove product", description = "Remove product by ID")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id) {
        var productToDelete = productService.findById(id);
        productService.delete(productToDelete);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a product", description = "Update the data of an existing user based on its ID")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        var updatedProduct = productService.update(id, productDTO.from());
        return ResponseEntity.ok(ProductDTO.fromDTO(updatedProduct));
    }
}
