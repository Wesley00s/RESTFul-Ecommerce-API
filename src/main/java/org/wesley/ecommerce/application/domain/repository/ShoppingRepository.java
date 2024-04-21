package org.wesley.ecommerce.application.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wesley.ecommerce.application.domain.model.Shopping;

public interface ShoppingRepository extends JpaRepository<Shopping, Long> {
}
