package com.example.productservice.service.implementation;

import com.example.productservice.entity.Product;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    @Value("${image.upload.directory}")
    private String uploadDir;
    private final ProductRepository productRepository;

    public List<String> uploadImages(Long productCode, List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();

        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new NotFoundException("Not found!"));

            for (MultipartFile image : images) {
                try {
                    // Create the file path for the uploaded image
                    String imagePath = uploadDir + "/" + image.getOriginalFilename();
                    File imageFile = new File(imagePath);

                    // Save the image to the directory
                    image.transferTo(imageFile);

                    // Add the URL to the list
                    String imageUrl = "http://localhost:8080/images/" + image.getOriginalFilename();
                    imageUrls.add(imageUrl);
                } catch (IOException e) {
                    // Handle the exception (e.g., log it and continue)
                    e.printStackTrace();
                }
            }

            // Add image URLs to product and save
            product.getImageUrls().addAll(imageUrls);
            productRepository.save(product);

        return imageUrls;
    }
}


