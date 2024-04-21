package org.wesley.ecommerce.application.service.implement;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.repository.CartItemRepository;
import org.wesley.ecommerce.application.domain.repository.ProductRepository;
import org.wesley.ecommerce.application.service.CartItemService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CartItemServiceImplement implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartItemServiceImplement(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Cart create(Cart cart) {
        return cartItemRepository.save(cart);
    }

    @Override
    public Cart findById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Cart> findAll() {
        return cartItemRepository.findAll();
    }

    @Override
    public void delete(Cart cart) {
        if(cartItemRepository.existsById(cart.getCartId())){
            cartItemRepository.deleteById(cart.getCartId());
        } else {
            throw new NoSuchElementException("Cart item with id " + cart.getCartId() + " found for delete");
        }
    }

    @Override
    public Cart getProducts(Long id, Long productId, Integer quantity) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product with id " + productId + " not found"));
        var cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found for ID: " + id));
        var currentStock = product.getStockQuantity();

        if (product.getStockQuantity() < quantity) {
            product.setStockQuantity(currentStock - quantity); ;
        } else {
            throw new IllegalArgumentException("Requested quantity exceeds available stock.");
        }
        return cartItemRepository.save(cartItem);
    }

}
