package org.wesley.ecommerce.application.service;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Product;

import java.util.List;

@Service
public interface ProductService {
    Product create(Product product);

    void create(List<Product> products);

    Product findById(Long id);

    List<Product> findAll();

    boolean isStockAvailable(Long productId, Integer quantity);

    Product update(Long id, Product product);

    void delete(Product product);

    void updateStock(Long productId, int quantity);

    List<Product> findProductsByCart(Long cartId);
}
