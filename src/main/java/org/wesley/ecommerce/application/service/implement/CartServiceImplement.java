package org.wesley.ecommerce.application.service.implement;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.enumeration.ItemStatus;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.CartItem;
import org.wesley.ecommerce.application.domain.model.Users;
import org.wesley.ecommerce.application.domain.repository.CartItemRepository;
import org.wesley.ecommerce.application.domain.repository.CartRepository;
import org.wesley.ecommerce.application.exceptions.BusinessException;
import org.wesley.ecommerce.application.exceptions.InsufficientStockException;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.ProductService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.wesley.ecommerce.application.utility.CalcAmount.recalculateCartTotal;


@Service
@AllArgsConstructor
public class CartServiceImplement implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final AuthenticationService authenticationService;

    @Override
    public Cart currentCart() {
        assert authenticationService != null;
        var user = authenticationService.getAuthenticatedUser();
        if (user == null) {
            throw new IllegalStateException("User is not authenticated");
        }
        return authenticationService.getActiveCart(user);
    }


    @Transactional
    @Override
    public void addProductToCart(Long productId, Integer quantity) {
        var cart = currentCart();

        var product = productService.findById(productId);
        if (product == null) {
            throw new EntityNotFoundException("Product not found.");
        }

        var existingPendingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .filter(item -> item.getStatus().equals(ItemStatus.PENDING))
                .findFirst();

        int totalQuantity = existingPendingItem.map(item -> item.getQuantity() + quantity).orElse(quantity);
        if (!productService.isStockAvailable(productId, totalQuantity)) {
            throw new InsufficientStockException(product.getName(), product.getId());
        }

        if (existingPendingItem.isPresent()) {
            var cartItem = existingPendingItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(newQuantity);
            cartItem.setPrice(product.getPrice() * newQuantity);
            cartItemRepository.save(cartItem);
        } else {
            Double itemPrice = product.getPrice() * quantity;
            var newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            newCartItem.setPrice(itemPrice);
            newCartItem.setStatus(ItemStatus.PENDING);
            cartItemRepository.save(newCartItem);
            cart.getItems().add(newCartItem);
        }

        recalculateCartTotal(cart);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void removeProductFromCart(Long productId, Integer quantity) {
        var cart = currentCart();

        var product = productService.findById(productId);
        if (product == null) {
            throw new EntityNotFoundException("Product not found.");
        }

        var cartItem = cart.getItems().stream()
                .filter(item -> productId.equals(item.getProduct().getId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Product is not in the cart"));

        if (cartItem.getQuantity() < quantity) {
            throw new BusinessException("Cannot remove more products than are in the cart");
        }

        if (quantity <= 0) {
            throw new BusinessException("Quantity must be greater than zero");
        }

        cartItem.setQuantity(cartItem.getQuantity() - quantity);
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());

        if (cartItem.getQuantity() == 0) {
            cart.getItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            cartItemRepository.save(cartItem);
        }

        recalculateCartTotal(cart);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public Boolean toggleItemSelection(Long itemId, Boolean selected) {
        var cart = currentCart();

        var cartItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Item is not in the cart"));

        if (!cartItem.getStatus().equals(ItemStatus.PENDING) && !cartItem.getStatus().equals(ItemStatus.UNSELECTED)) {
            throw new BusinessException("Only pending and unselected items can be toggled");
        }

        cartItem.setStatus(selected ? ItemStatus.PENDING : ItemStatus.UNSELECTED);
        cartItemRepository.save(cartItem);

        recalculateCartTotal(cart);
        cartRepository.save(cart);
        return cartItem.getStatus().equals(ItemStatus.PENDING);
    }

    @Override
    public Cart create(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart findById(Long id) {
        return cartRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public Cart update(Long cartId, Cart cart) {
        var cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isPresent()) {
            Cart cartExisting = cartOptional.get();

            if (cart.getTotalPrice() != null) {
                cartExisting.setTotalPrice(cart.getTotalPrice());
            }
            if (cart.getId() != null) {
                cartExisting.setId(cart.getId());
            }
            if (cart.getItems() != null) {
                cartExisting.setItems(cart.getItems());
            }
            if (cart.getUser() != null) {
                cartExisting.setUser(cart.getUser());
            }
            return cartRepository.save(cartExisting);
        } else {
            throw new NoSuchElementException("Cart not found for update");
        }
    }

    @Override
    public Cart findCartByUserId(UUID userId) {
        return cartRepository.findCartByUserId(userId);
    }
}
