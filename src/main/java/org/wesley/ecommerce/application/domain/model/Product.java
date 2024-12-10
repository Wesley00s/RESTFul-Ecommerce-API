package org.wesley.ecommerce.application.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imageUrl;
    @Column(unique = true)
    private String code;
    @Column(length = 10000)
    private String description;
    private Integer stock;
    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    @Column(nullable = false)
    private Double price;
}

