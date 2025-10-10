package org.wesley.ecommerce.application.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wesley.ecommerce.application.api.v1.controller.dto.request.CreateCommentRequest;
import org.wesley.ecommerce.application.api.v1.controller.dto.request.CreateReviewRequest;
import org.wesley.ecommerce.application.api.v1.controller.dto.request.UpdateProductRequest;
import org.wesley.ecommerce.application.api.v1.controller.dto.request.CreateProductRequest;
import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import org.wesley.ecommerce.application.domain.enumeration.ProductSortBy;
import org.wesley.ecommerce.application.domain.enumeration.SortDirection;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.domain.model.Users;

import java.util.List;
import java.util.UUID;

@Service
public interface ProductService {
    Product create(CreateProductRequest request, MultipartFile coverImageFile, List<MultipartFile> otherImageFiles);

    Product findById(Long id);

    Product findProductByCode(String code);

    Page<Product> findAll(ProductSortBy sortBy, SortDirection sortDirection, String name, Integer page, Integer pageSize);

    Page<Product> findProductsByCategory(ProductCategory category, Integer page, Integer pageSize);

    boolean isStockAvailable(Long productId, Integer quantity);

    Product update(Long id, UpdateProductRequest request, MultipartFile newCoverImage, List<MultipartFile> newImageFiles);

    void delete(Product product);

    List<Product> findProductsByCart(Long cartId);

    void submitReview(Long productId, Users authenticatedUser, CreateReviewRequest reviewRequest);

    void submitComment(Long productId, UUID reviewId, Users authenticatedUser, CreateCommentRequest request);
}
