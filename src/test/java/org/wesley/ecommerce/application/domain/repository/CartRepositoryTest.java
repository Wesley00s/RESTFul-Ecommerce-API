package org.wesley.ecommerce.application.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.Users;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CartRepositoryTest {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should find cart by user ID successfully")
    void findCartByUserIdSuccessfully() {
        // Arrange
        Users user = new Users();
        user.setEmail("test@email.com");
        entityManager.persist(user);

        Cart cart = new Cart();
        cart.setUsers(user);
        entityManager.persist(cart);

        // Act
        Cart retrievedCart = cartRepository.findCartByUserId(user.getId());

        // Assert
        assertNotNull(retrievedCart, "The cart should not be null");
        assertEquals(cart.getId(), retrievedCart.getId(), "The retrieved cart should have the same ID");
        assertEquals(cart.getUsers().getId(), retrievedCart.getUsers().getId(),
                "The retrieved cart should belong to the correct user");
    }

    @Test
    @DisplayName("Should return null when no cart is found for the given user ID")
    void findCartByUserIdFailure() {
        // Arrange
        Users user = new Users();
        user.setEmail("test@email.com");
        entityManager.persist(user);

        // Act
        Cart retrievedCart = cartRepository.findCartByUserId(user.getId());

        // Assert
        assertNull(retrievedCart, "The retrieved cart should be null when no cart is associated with the user");
    }
}