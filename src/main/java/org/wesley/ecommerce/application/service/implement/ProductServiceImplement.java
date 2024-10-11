package org.wesley.ecommerce.application.service.implement;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.domain.repository.ProductRepository;
import org.wesley.ecommerce.application.service.ProductService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductServiceImplement implements ProductService {
    final private ProductRepository productRepository;

    public ProductServiceImplement(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(Product product) {

        return productRepository.save(product);
    }

    @Override
    public Product findById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product update(Long id, Product product) {

        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product productExist = productOptional.get();
            productExist.setName(product.getName());
            productExist.setDescription(product.getDescription());
            productExist.setPrice(product.getPrice());
            productExist.setStockQuantity(product.getStockQuantity());
            productExist.setImageUrl(product.getImageUrl());
            productExist.setCategory(product.getCategory());
            return productRepository.save(productExist);
        } else {
            throw new NoSuchElementException("Product not found for update");
        }
    }

    /**
     * Deletes a product from the database.
     *
     * @param product the product to be deleted
     * @throws NoSuchElementException if the product is not found
     */
    @Override
    public void delete(Product product) {

        if (productRepository.existsById(product.getId())) {
            productRepository.deleteById(product.getId());
        } else {
            throw new NoSuchElementException("Product with id " + product.getId() + " not found for delete");
        }
    }

    @Override
    public void updateQuantityStock(Long productId, int quantity) {
        productRepository.updateQuantityStock(productId, quantity);
    }

    @Override
    public List<Product> findProductsByCart(Long cartId) {
        var products = productRepository.findProductsByCart(cartId);
        if (products.isEmpty()) {
            throw new NoSuchElementException("No products found for cart " + cartId);
        }
        return productRepository.findProductsByCart(cartId);
    }
}
