package org.wesley.ecommerce.application.domain.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.wesley.ecommerce.application.domain.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cart_item (cart_id, product_id, quantity, price) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    void addToCart(Long cartId, Long productId, Integer quantity, Double price);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_item WHERE product_id = ?1 AND cart_id = ?2", nativeQuery = true)
    void removeAllFromCart(Long productId, Long cartId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cart_item SET price = ?1 WHERE product_id = ?2 AND cart_id = ?3", nativeQuery = true)
    void updateTotalPrice(Double newTotalPrice, Long productId, Long cartId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cart_item SET quantity = quantity -?3 WHERE product_id = ?1 AND cart_id = ?2 AND quantity >= ?3", nativeQuery = true)
    void removeOnlyFromCartItem(Long productId, Long cartId, int quantity);
}
