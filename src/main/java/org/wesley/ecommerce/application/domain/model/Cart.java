package org.wesley.ecommerce.application.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wesley.ecommerce.application.domain.enumeration.ItemStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
    private Double totalPrice = 0.0;


    public void recalculateTotal() {
        this.totalPrice = items.stream()
                .filter(item -> item.getStatus() == ItemStatus.PENDING)
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}