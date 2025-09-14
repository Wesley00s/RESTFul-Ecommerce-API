package org.wesley.ecommerce.application.service.implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public record UploadResult(String url, String publicId) {
    }

    public List<UploadResult> uploadFiles(List<MultipartFile> files) {
        List<UploadResult> results = new ArrayList<>();

        Map<String, Object> options = Map.of(
                "folder", "ecommerce",
                "resource_type", "auto"
        );

        for (MultipartFile file : files) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

                String url = uploadResult.get("secure_url").toString();
                String publicId = uploadResult.get("public_id").toString();

                results.add(new UploadResult(url, publicId));

            } catch (IOException e) {
                throw new RuntimeException("An error occurred while uploading file: " + file.getOriginalFilename(), e);
            }
        }
        return results;
    }

    public UploadResult uploadFile(MultipartFile file) {
        try {
            Map<String, Object> options = Map.of(
                    "folder", "ecommerce",
                    "resource_type", "auto"
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

            String url = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();

            return new UploadResult(url, publicId);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while load file: " + e.getMessage(), e);
        }
    }


    public void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while deleting de image: " + e.getMessage(), e);
        }
    }
}