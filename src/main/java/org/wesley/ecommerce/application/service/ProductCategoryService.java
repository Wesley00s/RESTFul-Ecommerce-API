package org.wesley.ecommerce.application.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wesley.ecommerce.application.api.v1.controller.dto.request.*;
import org.wesley.ecommerce.application.domain.model.ProductCategory;

@Service
public interface ProductCategoryService {
    ProductCategory create(CreateProductCategoryRequest request, MultipartFile imageFile);

    ProductCategory findById(Long id);

    Page<ProductCategory> findAll(String name, Integer page, Integer pageSize);

    ProductCategory update(Long id, UpdateProductCategoryRequest request, MultipartFile newImage);

    void delete(ProductCategory category);
}
