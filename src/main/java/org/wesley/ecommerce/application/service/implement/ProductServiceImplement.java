package org.wesley.ecommerce.application.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import org.wesley.ecommerce.application.domain.enumeration.ProductSortBy;
import org.wesley.ecommerce.application.domain.enumeration.SortDirection;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.domain.repository.CartRepository;
import org.wesley.ecommerce.application.domain.repository.ProductRepository;
import org.wesley.ecommerce.application.service.ProductService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImplement implements ProductService {
    final private ProductRepository productRepository;
    private final CartRepository cartRepository;

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
    public Page<Product> findAll(ProductSortBy sortBy, SortDirection sortDirection, String name, Integer page, Integer pageSize) {

        var finalSortBy = (sortBy == null) ? ProductSortBy.NAME : sortBy;
        Sort.Direction finalDirection = (sortDirection == null || sortDirection == SortDirection.ASC)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        var pageable = PageRequest.of(
                page,
                pageSize,
                Sort.by(finalDirection, finalSortBy.getPropertyName())
        );

        if (name != null && !name.isBlank() && !name.trim().isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(name, pageable);
        }
        return productRepository.findAll(pageable);
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
            if (product.getCoverImageUrl() != null) {
                productExist.setCoverImageUrl(product.getCoverImageUrl());
            }
            if (product.getImageUrls() != null) {
                productExist.setImageUrls(product.getImageUrls());
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
    public List<Product> findProductsByCart(Long cartId) {
        var cart = cartRepository.findById(cartId);
        if (cart.isEmpty()) {
            throw new NoSuchElementException("Cart not found.");
        }
        return productRepository.findProductsByCart(cartId);
    }
}
