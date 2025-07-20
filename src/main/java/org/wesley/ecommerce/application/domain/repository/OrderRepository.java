package org.wesley.ecommerce.application.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.wesley.ecommerce.application.domain.model.OrderShopping;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderShopping, Long> {
    @Query(
            value = "SELECT o FROM OrderShopping o LEFT JOIN FETCH o.items",
            countQuery = "SELECT COUNT(o) FROM OrderShopping o"
    )
    Page<OrderShopping> findAllWithItems(Pageable pageable);

    List<OrderShopping> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
