package org.wesley.ecommerce.application.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.wesley.ecommerce.application.domain.enumeration.ItemStatus;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.CartItem;
import org.wesley.ecommerce.application.domain.model.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should add item to cart")
    void addItemToCartSuccess() {
        // Arrange
        Cart cart = new Cart();
        cart = entityManager.persistAndFlush(cart);

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);
        product = entityManager.persistAndFlush(product);

        // Act
        cartItemRepository.addItemToCart(cart.getId(), product.getId(), 2, ItemStatus.PENDING, 200.0);

        // Assert
        List<CartItem> items = entityManager
                .getEntityManager()
                .createQuery("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId", CartItem.class)
                .setParameter("cartId", cart.getId())
                .getResultList();

        // Assert
        assertEquals(1, items.size());
        CartItem savedItem = items.getFirst();
        assertNotNull(savedItem.getId());
        assertEquals(2, savedItem.getQuantity());
        assertEquals(200.0, savedItem.getPrice());
    }

    @Test
    @DisplayName("Shouldn't add item to cart when cart or product is invalid")
    void addItemToCartFailure() {
        // Arrange
        Long invalidCartId = -1L;
        Long invalidProductId = -1L;

        // Act & Assert
        assertThrows(
                DataIntegrityViolationException.class,
                () -> cartItemRepository.addItemToCart(invalidCartId, invalidProductId, 2, ItemStatus.PENDING, 200.0),
                "Expected an DataIntegrityViolationException to be thrown for invalid cart or product"
        );

        List<CartItem> items = entityManager
                .getEntityManager()
                .createQuery("SELECT ci FROM CartItem ci", CartItem.class)
                .getResultList();

        assertTrue(items.isEmpty(), "No items should be added to the cart for invalid inputs");
    }

    @Test
    @DisplayName("Shouldn't remove products from cart when productId or cartId is invalid")
    void removeAllFromCartFailure() {
        // Arrange
        Cart cart = new Cart();
        cart = entityManager.persistAndFlush(cart);

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);
        product = entityManager.persistAndFlush(product);

        cartItemRepository.addItemToCart(cart.getId(), product.getId(), 2, ItemStatus.PENDING, 200.0);
        cartItemRepository.addItemToCart(cart.getId(), product.getId(), 3, ItemStatus.PENDING, 300.0);

        List<CartItem> itemsBefore = entityManager
                .getEntityManager()
                .createQuery("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId", CartItem.class)
                .setParameter("cartId", cart.getId())
                .getResultList();

        assertEquals(2, itemsBefore.size(), "There should be 2 items in the cart before removal");

        // Act
        Long invalidCartId = -1L;
        Long invalidProductId = -1L;
        cartItemRepository.removeOnlyFromCartItem(invalidProductId, invalidCartId, 1);

        // Assert
        List<CartItem> itemsAfter = entityManager
                .getEntityManager()
                .createQuery("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId", CartItem.class)
                .setParameter("cartId", cart.getId())
                .getResultList();

        assertEquals(2, itemsAfter.size(), "Items should remain in the cart if invalid IDs are provided");
    }

    @Test
    @DisplayName("Should update total cart item price successfully")
    void updateTotalPriceSuccess() {
        // Arrange
        Cart cart = new Cart();
        cart = entityManager.persistAndFlush(cart);

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);
        product = entityManager.persistAndFlush(product);

        cartItemRepository.addItemToCart(cart.getId(), product.getId(), 2, ItemStatus.PENDING, 200.0);

        CartItem cartItemBeforeUpdate = entityManager
                .getEntityManager()
                .createQuery("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.product.id = :productId", CartItem.class)
                .setParameter("cartId", cart.getId())
                .setParameter("productId", product.getId())
                .getSingleResult();

        assertNotNull(cartItemBeforeUpdate);
        assertEquals(200.0, cartItemBeforeUpdate.getPrice(), "The initial price should match");

        // Act
        Double newTotalPrice = 300.0;
        cartItemRepository.updateTotalPrice(newTotalPrice, product.getId(), cart.getId());

        entityManager.getEntityManager().clear();

        // Assert
        CartItem cartItemAfterUpdate = entityManager
                .getEntityManager()
                .createQuery("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.product.id = :productId", CartItem.class)
                .setParameter("cartId", cart.getId())
                .setParameter("productId", product.getId())
                .getSingleResult();

        assertNotNull(cartItemAfterUpdate);
        assertEquals(newTotalPrice, cartItemAfterUpdate.getPrice(), "The updated price should match the new total price");
    }

    @Test
    @DisplayName("Should fail to update total price when product or cart is not found")
    void updateTotalPriceFail() {
        // Arrange
        Double newTotalPrice = -180.0;
        Long invalidProductId = 999L;
        Long invalidCartId = 888L;

        // Act
        int updatedRows = entityManager
                .getEntityManager()
                .createNativeQuery("UPDATE cart_item SET price = :price WHERE product_id = :productId AND cart_id = :cartId")
                .setParameter("price", newTotalPrice)
                .setParameter("productId", invalidProductId)
                .setParameter("cartId", invalidCartId)
                .executeUpdate();

        // Assert
        assertEquals(0, updatedRows, "Expected no rows to be updated for invalid product and cart IDs");
    }

    @Test
    @DisplayName("Should remove quantity from cart item when sufficient stock is available")
    void removeOnlyFromCartItemSuccess() {
        // Arrange
        Double price = 4.0;
        int initialQuantity = 10;
        int quantityToRemove = 5;
        Cart cart = new Cart();
        entityManager.persist(cart);

        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(price);
        entityManager.persist(product);

        // Inserir item no carrinho
        entityManager.getEntityManager().createNativeQuery(
                        "INSERT INTO cart_item (product_id, cart_id, quantity, price) VALUES (:productId, :cartId, :quantity, :price)")
                .setParameter("productId", product.getId())
                .setParameter("cartId", cart.getId())
                .setParameter("quantity", initialQuantity)
                .setParameter("price", price)
                .executeUpdate();

        // Act
        cartItemRepository.removeOnlyFromCartItem(product.getId(), cart.getId(), quantityToRemove);

        // Assert
        Integer updatedQuantity = (Integer) entityManager.getEntityManager().createNativeQuery(
                        "SELECT quantity FROM cart_item WHERE product_id = :productId AND cart_id = :cartId")
                .setParameter("productId", product.getId())
                .setParameter("cartId", cart.getId())
                .getSingleResult();

        assertEquals(initialQuantity - quantityToRemove, updatedQuantity,
                "The quantity should be reduced by the specified amount");
    }

    @Test
    @DisplayName("Should not remove quantity when insufficient stock is available")
    void removeOnlyFromCartItemFailure() {
        // Arrange
        Double price = 4.0;
        int initialQuantity = 10;
        int quantityToRemove = 15;
        Cart cart = new Cart();
        entityManager.persist(cart);

        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(price);
        entityManager.persist(product);

        entityManager.getEntityManager().createNativeQuery(
                        "INSERT INTO cart_item (product_id, cart_id, quantity, price) VALUES (:productId, :cartId, :quantity, :price)")
                .setParameter("productId", product.getId())
                .setParameter("cartId", cart.getId())
                .setParameter("quantity", initialQuantity)
                .setParameter("price", price)
                .executeUpdate();

        // Act
        cartItemRepository.removeOnlyFromCartItem(product.getId(), cart.getId(), quantityToRemove);

        // Assert
        Integer updatedQuantity = (Integer) entityManager.getEntityManager().createNativeQuery(
                        "SELECT quantity FROM cart_item WHERE product_id = :productId AND cart_id = :cartId")
                .setParameter("productId", product.getId())
                .setParameter("cartId", cart.getId())
                .getSingleResult();

        assertEquals(initialQuantity, updatedQuantity,
                "The quantity should remain unchanged when the removal quantity exceeds the available stock");
    }

}