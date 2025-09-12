package org.wesley.ecommerce.application.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String coverImageUrl;
    @ElementCollection
    @CollectionTable(
            name = "product_image_urls",
            joinColumns = @JoinColumn(name = "product_id")
    )
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();
    @Column(unique = true)
    private String code;
    @Column(length = 10000)
    private String description;
    private Integer stock;
    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false, columnDefinition = "default 0")
    private Double rating = 0.0;
    @Column(nullable = false, columnDefinition = "default 0")
    private Integer soldCount = 0;
    @Column(nullable = false)
    private Boolean isAvailable = true;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

