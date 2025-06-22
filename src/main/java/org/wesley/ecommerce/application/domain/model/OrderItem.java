package org.wesley.ecommerce.application.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import org.wesley.ecommerce.application.domain.enumeration.ItemStatus;

@Entity
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderShopping order;
    
    @ManyToOne
    private Product product;
    
    private Integer quantity;
    private Double price;
    private ItemStatus status = ItemStatus.PENDING;
}