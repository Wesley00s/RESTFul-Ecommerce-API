package org.wesley.ecommerce.application.service;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Product;

import java.util.List;

@Service
public interface ProductService {
    Product create(Product product);
    Product findById(Long id);
    List<Product> findAll();
    Product update(Long id, Product product);
    void delete(Product product);
}
