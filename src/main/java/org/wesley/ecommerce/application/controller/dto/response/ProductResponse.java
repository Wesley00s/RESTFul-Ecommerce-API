package org.wesley.ecommerce.application.controller.dto.response;

import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import org.wesley.ecommerce.application.domain.model.Product;

import java.time.LocalDateTime;
import java.util.Map;


public record ProductResponse(
        Long id,
        String name,
        String coverImageUrl,
        String coverImagePublicId,
        Map<String, String> imageUrls,
        String code,
        String description,
        Integer stock,
        ProductCategory category,
        Double price,
        Double rating,
        Integer soldCount,
        Boolean isAvailable,
        LocalDateTime createdAt

) {

    public static ProductResponse fromDTO(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCoverImageUrl(),
                product.getCoverImagePublicId(),
                product.getImageUrls(),
                product.getCode(),
                product.getDescription(),
                product.getStock(),
                product.getCategory(),
                product.getPrice(),
                product.getRating(),
                product.getSoldCount(),
                product.getIsAvailable(),
                product.getCreatedAt()
        );
    }

    public Product from(String code) {
        var product = new Product();
        product.setName(name());
        product.setCoverImageUrl(coverImageUrl());
        product.setCoverImagePublicId(coverImagePublicId());
        product.setImageUrls(imageUrls());
        product.setCode(code);
        product.setDescription(description());
        product.setStock(stock());
        product.setCategory(category());
        product.setPrice(price());
        product.setRating(rating());

        return product;
    }
}
