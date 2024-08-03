package org.wesley.ecommerce.application.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "db_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    private String name;
    @Column(length = 10000)
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
    private ProductCategory category;
}
