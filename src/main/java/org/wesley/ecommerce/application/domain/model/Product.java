package org.wesley.ecommerce.application.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    private String coverImagePublicId;

    @ElementCollection
    @CollectionTable(
            name = "product_images",
            joinColumns = @JoinColumn(name = "product_id")
    )
    @MapKeyColumn(name = "image_public_id")
    @Column(name = "image_url")
    private Map<String, String> imageUrls = new HashMap<>();

    @Column(unique = true)
    private String code;

    @Column(length = 10000)
    private String description;

    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false, columnDefinition = "default 0")
    private Double rating = 0.0;

    @Column(nullable = false, columnDefinition = "default 0")
    private Long totalReviews = 0L;

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

