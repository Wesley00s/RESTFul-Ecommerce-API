package org.wesley.ecommerce.application.controller.dto.request;

import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import org.wesley.ecommerce.application.domain.model.Product;

import java.util.List;


public record ProductRequest(
        String name,
        String coverImageUrl,
        List<String> imageUrls,
        String description,
        Integer stock,
        ProductCategory category,
        Double price
) {

    public static Product getProduct(
            String name,
            String coverImageUrl,
            List<String> imageUrls,
            String code,
            String description,
            Integer stock,
            ProductCategory category,
            Double price
    ) {
        Product product = new Product();
        product.setName(name);
        product.setCoverImageUrl(coverImageUrl);
        product.getImageUrls().addAll(imageUrls);
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
                this.coverImageUrl,
                this.imageUrls,
                code,
                this.description,
                this.stock,
                this.category,
                this.price
        );
    }
}
