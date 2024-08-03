package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import org.wesley.ecommerce.application.domain.model.Product;

import java.math.BigDecimal;

/**
 * A data transfer object (DTO) representing a product in the e-commerce application.
 * This class is used to transfer product data between the application layers.
 *
 * @param name          the name of the product
 * @param description   a brief description of the product
 * @param price         the price of the product
 * @param stockQuantity the quantity of the product in stock
 * @param imageUrl      the URL of the product image
 * @param category      the category of the product
 */
public record ProductDTO(
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String imageUrl,
        ProductCategory category
) {

    /**
     * Creates a new {@link ProductDTO} instance from a given {@link Product} entity.
     *
     * @param product the {@link Product} entity to create the DTO from
     * @return a new {@link ProductDTO} instance
     */
    public static ProductDTO fromDTO(Product product) {
        return new ProductDTO(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategory()
        );
    }

    /**
     * Creates a new {@link Product} entity from the current {@link ProductDTO} instance.
     *
     * @return a new {@link Product} entity
     */
    public Product from() {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
        product.setImageUrl(imageUrl);
        product.setCategory(category);
        return product;
    }
}
