package org.wesley.ecommerce.application.service.implement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
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
    public void create(List<Product> products) {
        productRepository.saveAll(products);
    }

    @Override
    public Product findById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Page<Product> findAll(Integer page, Integer pageSize) {
        return productRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Page<Product> findProductsByCategory(ProductCategory category, Integer page, Integer pageSize) {
        return productRepository.findProductsByCategory(category, PageRequest.of(page, pageSize));
    }

    @Override
    public boolean isStockAvailable(Long productId, Integer quantity) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found."));
        return product.getStock() >= quantity;
    }

    @Override
    public Product update(Long id, Product product) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product productExist = productOptional.get();

            if (product.getName() != null) {
                productExist.setName(product.getName());
            }
            if (product.getPrice() != null) {
                productExist.setPrice(product.getPrice());
            }
            if (product.getImageUrl() != null) {
                productExist.setImageUrl(product.getImageUrl());
            }
            if (product.getCategory() != null) {
                productExist.setCategory(product.getCategory());
            }
            if (product.getDescription() != null) {
                productExist.setDescription(product.getDescription());
            }
            if (product.getStock() != null) {
                productExist.setStock(product.getStock());
            }

            return productRepository.save(productExist);
        } else {
            throw new NoSuchElementException("Product not found for update");
        }
    }

    @Override
    public void delete(Product product) {

        if (productRepository.existsById(product.getId())) {
            productRepository.deleteById(product.getId());
        } else {
            throw new NoSuchElementException("Product with id " + product.getId() + " not found for delete");
        }
    }

    @Override
    public void updateStock(Long productId, int quantity) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found."));
        product.setStock(quantity);
        productRepository.save(product);
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
