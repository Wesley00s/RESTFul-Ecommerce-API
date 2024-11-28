package org.wesley.ecommerce.application.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wesley.ecommerce.application.domain.model.OrderShopping;

public interface OrderRepository extends JpaRepository<OrderShopping, Long> {
}
