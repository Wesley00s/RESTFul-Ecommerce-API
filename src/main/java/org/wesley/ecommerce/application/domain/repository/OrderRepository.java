package org.wesley.ecommerce.application.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.wesley.ecommerce.application.domain.model.OrderShopping;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderShopping, Long> {
    @Query("SELECT o FROM OrderShopping o JOIN FETCH o.items")
    List<OrderShopping> findAllWithItems();

    List<OrderShopping> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
