package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.wesley.ecommerce.application.controller.dto.ProductDTO;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.service.ProductService;

import java.util.List;

/**
 * Controller for managing products in the e-commerce application.
 *
 * @author Wesley
 */
@RestController
@RequestMapping("/product")
@Tag(name = "Product Controller", description = "RESTFul API for managing products.")
public class ProductController {
    final private ProductService productService;

    /**
     * Constructor for the ProductController.
     *
     * @param productService The service for managing products.
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Creates a new product.
     *
     * @param product The DTO of the product to be created.
     * @return The ResponseEntity containing the created product's data and a 201 Created status.
     */
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

    /**
     * Retrieves a list of all registered products.
     *
     * @return The ResponseEntity containing a list of all products and a 200 OK status.
     */
    @GetMapping("/products")
    @Operation(summary = "Get all products", description = "Retrieve a list of all registered products")
    public ResponseEntity<List<Product>> getAllProducts() {
        var products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    /**
     * Retrieves a specific product based on its ID.
     *
     * @param id The ID of the product to be retrieved.
     * @return The ResponseEntity containing the requested product and a 200 OK status.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Retrieve a specific product based on its ID")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        var product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Removes a product based on its ID.
     *
     * @param id The ID of the product to be removed.
     * @return The ResponseEntity with a 204 No Content status.
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Remove product", description = "Remove product by ID")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id) {
        var productToDelete = productService.findById(id);
        productService.delete(productToDelete);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the data of an existing product based on its ID.
     *
     * @param id The ID of the product to be updated.
     * @param productDTO The DTO of the updated product.
     * @return The ResponseEntity containing the updated product's data and a 200 OK status.
     */
    @PutMapping("/update/{id}")
    @Operation(summary = "Update a product", description = "Update the data of an existing user based on its ID")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        var updatedProduct = productService.update(id, productDTO.from());
        return ResponseEntity.ok(ProductDTO.fromDTO(updatedProduct));
    }
}
