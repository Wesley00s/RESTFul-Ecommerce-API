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
            return productRepository.save(productExist);
        } else {
            throw new NoSuchElementException("Product not found for update");
        }
    }

    @Override
    public void delete(Product product) {
        if(productRepository.existsById(product.getProductId())) {
            productRepository.deleteById(product.getProductId());
        } else {
            throw new NoSuchElementException("Product with id " + product.getProductId() + " not found for delete");
        }
    }
}
