package org.wesley.ecommerce.application.service.implement;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wesley.ecommerce.application.api.v1.controller.dto.event.CommentAddedEvent;
import org.wesley.ecommerce.application.api.v1.controller.dto.event.ReviewCreatedEvent;
import org.wesley.ecommerce.application.api.v1.controller.dto.request.CreateCommentRequest;
import org.wesley.ecommerce.application.api.v1.controller.dto.request.CreateReviewRequest;
import org.wesley.ecommerce.application.api.v1.controller.dto.request.UpdateProductRequest;
import org.wesley.ecommerce.application.api.v1.controller.dto.request.CreateProductRequest;
import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import org.wesley.ecommerce.application.domain.enumeration.ProductSortBy;
import org.wesley.ecommerce.application.domain.enumeration.SortDirection;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.domain.model.Users;
import org.wesley.ecommerce.application.domain.repository.CartRepository;
import org.wesley.ecommerce.application.domain.repository.ProductCategoryRepository;
import org.wesley.ecommerce.application.domain.repository.ProductRepository;
import org.wesley.ecommerce.application.exceptions.local.ProductCategoryNotFoundException;
import org.wesley.ecommerce.application.service.ProductService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.wesley.ecommerce.application.utility.CodeGenerate.randomCode;

@Service
@AllArgsConstructor
public class ProductServiceImplement implements ProductService {
    final private ProductRepository productRepository;
    final private ProductCategoryRepository productCategoryRepository;
    private final CartRepository cartRepository;
    private final CloudinaryService cloudinaryService;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public Product create(CreateProductRequest request, MultipartFile coverImageFile, List<MultipartFile> otherImageFiles) {
        var category = productCategoryRepository.findById(request.categoryId()).orElseThrow(() ->
                new ProductCategoryNotFoundException(request.categoryId())
        );

        CloudinaryService.UploadResult coverUploadResult = cloudinaryService.uploadFile(coverImageFile);
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setCoverImageUrl(coverUploadResult.url());
        product.setCoverImagePublicId(coverUploadResult.publicId());
        product.setCategory(category);
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setCode(randomCode());

        product.getImageUrls().put(coverUploadResult.publicId(), coverUploadResult.url());

        if (otherImageFiles != null && !otherImageFiles.isEmpty()) {
            List<CloudinaryService.UploadResult> otherImagesResults = cloudinaryService.uploadFiles(otherImageFiles);
            for (var result : otherImagesResults) {
                product.getImageUrls().put(result.publicId(), result.url());
            }
        }

        return productRepository.save(product);
    }

    @Override
    public Product findById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Product findProductByCode(String code) {
        return productRepository.findProductByCode(code);
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

    @Transactional
    @Override
    public Product update(Long id, UpdateProductRequest request, MultipartFile newCoverImage, List<MultipartFile> newImageFiles) {
        var category = productCategoryRepository.findById(request.categoryId()).orElseThrow(() ->
                new ProductCategoryNotFoundException(request.categoryId())
        );

        Product productExist = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found for update"));

        if (request.publicIdsToDelete() != null && !request.publicIdsToDelete().isEmpty()) {
            for (String publicId : request.publicIdsToDelete()) {
                cloudinaryService.deleteImage(publicId);
                productExist.getImageUrls().remove(publicId);

                if (publicId.equals(productExist.getCoverImagePublicId())) {
                    productExist.setCoverImageUrl(null);
                    productExist.setCoverImagePublicId(null);
                }
            }
        }

        if (newCoverImage != null && !newCoverImage.isEmpty()) {
            if (productExist.getCoverImagePublicId() != null) {
                cloudinaryService.deleteImage(productExist.getCoverImagePublicId());
            }
            var coverUploadResult = cloudinaryService.uploadFile(newCoverImage);
            productExist.setCoverImageUrl(coverUploadResult.url());
            productExist.setCoverImagePublicId(coverUploadResult.publicId());
            productExist.getImageUrls().put(coverUploadResult.publicId(), coverUploadResult.url());
        }

        if (newImageFiles != null && !newImageFiles.isEmpty()) {
            var uploadResults = cloudinaryService.uploadFiles(newImageFiles);
            for (var result : uploadResults) {
                productExist.getImageUrls().put(result.publicId(), result.url());
            }
        }

        if (request.name() != null) productExist.setName(request.name());
        if (request.price() != null) productExist.setPrice(request.price());
        if (category != null) productExist.setCategory(category);
        if (request.description() != null) productExist.setDescription(request.description());
        if (request.stock() != null) productExist.setStock(request.stock());

        return productRepository.save(productExist);
    }

    @Override
    public void delete(Product product) {

        if (productRepository.existsById(product.getId())) {
            productRepository.deleteById(product.getId());
            cloudinaryService.deleteImage(product.getCoverImagePublicId());
            for (var imageUrl : product.getImageUrls().keySet()) {
                cloudinaryService.deleteImage(imageUrl);
            }
            cloudinaryService.deleteImage(product.getCoverImagePublicId());

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

    @Override
    public void submitReview(Long productId, Users authenticatedUser, CreateReviewRequest request) {
        var product = this.findById(productId);
        if (product == null) {
            throw new NoSuchElementException("Product not found.");
        }

        var event = new ReviewCreatedEvent(
                UUID.randomUUID().toString(), 
                productId,
                product.getCode(),
                authenticatedUser.getId(),
                authenticatedUser.getName(),
                request.rating(),
                request.content()
        );

        String reviewCreatedQueue = "review.created.queue";
        rabbitTemplate.convertAndSend(reviewCreatedQueue, event);
    }

    @Override
    public void submitComment(Long productId, UUID reviewId, Users authenticatedUser, CreateCommentRequest request) {
        var event = new CommentAddedEvent(
                reviewId,
                UUID.randomUUID().toString(), 
                request.parentCommentId(),    
                authenticatedUser.getId(),
                authenticatedUser.getName(),
                request.content(),
                request.mentionedUserId(),
                request.mentionedUserName()
        );

        String commentAddedQueue = "comment.added.queue"; 
        rabbitTemplate.convertAndSend(commentAddedQueue, event);
    }
}
