package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import org.wesley.ecommerce.application.domain.model.Product;


public record ProductRequestDTO(
        String name,
        String imageUrl,
        String description,
        Integer stock,
        ProductCategory category,
        Double price
) {

    public static ProductRequestDTO fromDTO(Product product) {
        return new ProductRequestDTO(
                product.getName(),
                product.getImageUrl(),
                product.getDescription(),
                product.getStock(),
                product.getCategory(),
                product.getPrice()
        );
    }

    static Product getProduct(
            String name,
            String imageUrl,
            String code,
            String description,
            Integer stock,
            ProductCategory category,
            Double price
    ) {
        Product product = new Product();
        product.setName(name);
        product.setImageUrl(imageUrl);
        product.setCode(code);
        product.setDescription(description);
        product.setStock(stock);
        product.setCategory(category);
        product.setPrice(price);
        return product;
    }

    public Product from(String code) {
        return getProduct(
                this.name,
                this.imageUrl,
                code,
                this.description,
                this.stock,
                this.category,
                this.price
        );
    }
}
