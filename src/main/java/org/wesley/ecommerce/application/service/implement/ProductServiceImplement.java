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

    /**
     * Constructor for ProductServiceImplement.
     *
     * @param productRepository the product repository to be used for database operations
     */
    public ProductServiceImplement(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(Product product) {

        return productRepository.save(product);
    }

    /**
     * Finds a product by its id.
     *
     * @param id the id of the product to be found
     * @return the found product, or throws a NoSuchElementException if not found
     */
    @Override
    public Product findById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Product> findAll() {
        /**
         * Finds all products in the database.
         *
         * @return a list of all products
         */
        return productRepository.findAll();
    }

    /**
     * Updates an existing product in the database.
     *
     * @param id the id of the product to be updated
     * @param product the updated product
     * @return the updated product, or throws a NoSuchElementException if not found
     */
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

        if(productRepository.existsById(product.getProductId())) {
            productRepository.deleteById(product.getProductId());
        } else {
            throw new NoSuchElementException("Product with id " + product.getProductId() + " not found for delete");
        }
    }
}
