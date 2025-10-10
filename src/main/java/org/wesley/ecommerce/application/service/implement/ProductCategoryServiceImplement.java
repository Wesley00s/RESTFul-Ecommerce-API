package org.wesley.ecommerce.application.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wesley.ecommerce.application.api.v1.controller.dto.request.*;
import org.wesley.ecommerce.application.domain.model.ProductCategory;
import org.wesley.ecommerce.application.domain.repository.ProductCategoryRepository;
import org.wesley.ecommerce.application.service.ProductCategoryService;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ProductCategoryServiceImplement implements ProductCategoryService {
    final private ProductCategoryRepository productCategoryRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public ProductCategory create(CreateProductCategoryRequest request, MultipartFile imageFile) {
        CloudinaryService.UploadResult imageUploadResult = cloudinaryService.uploadFile(imageFile);
        var productCategory = new ProductCategory();

        productCategory.setName(request.name());
        productCategory.setDescription(request.description());
        productCategory.setImageUrl(imageUploadResult.url());
        productCategory.setImagePublicId(imageUploadResult.publicId());

        return productCategoryRepository.save(productCategory);
    }

    @Override
    public ProductCategory findById(Long id) {
        return productCategoryRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Page<ProductCategory> findAll(String name, Integer page, Integer pageSize) {
        var pageable = PageRequest.of(page, pageSize);

        if (name != null && !name.isBlank() && !name.trim().isEmpty()) {
            return productCategoryRepository.findByNameContainingIgnoreCase(name, pageable);
        }
        return productCategoryRepository.findAll(pageable);
    }

    @Override
    public ProductCategory update(Long id, UpdateProductCategoryRequest request, MultipartFile newImage) {
        var productCategoryExist = productCategoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found for update"));

        if (newImage != null && !newImage.isEmpty()) {
            if (productCategoryExist.getImagePublicId() != null) {
                cloudinaryService.deleteImage(productCategoryExist.getImagePublicId());
            }
            var coverUploadResult = cloudinaryService.uploadFile(newImage);
            productCategoryExist.setImageUrl(coverUploadResult.url());
            productCategoryExist.setImagePublicId(coverUploadResult.publicId());
        }

        if (request.name() != null) productCategoryExist.setName(request.name());
        if (request.description() != null) productCategoryExist.setDescription(request.description());

        return productCategoryRepository.save(productCategoryExist);
    }

    @Override
    public void delete(ProductCategory category) {
        if (productCategoryRepository.existsById(category.getId())) {
            productCategoryRepository.deleteById(category.getId());
            cloudinaryService.deleteImage(category.getImagePublicId());

            cloudinaryService.deleteImage(category.getImagePublicId());

        } else {
            throw new NoSuchElementException("Category with id " + category.getId() + " not found for delete");
        }
    }
}
