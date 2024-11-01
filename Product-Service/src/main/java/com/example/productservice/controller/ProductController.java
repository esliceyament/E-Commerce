package com.example.productservice.controller;

import com.example.productservice.dto.PageDto;
import com.example.productservice.dto.ProductDto;
import com.example.productservice.payload.ProductPayload;
import com.example.productservice.response.ProductResponse;
import com.example.productservice.service.ProductService;
import com.example.productservice.service.implementation.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ImageUploadService imageUploadService;

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductPayload payload, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return ResponseEntity.ok(productService.addProduct(payload, authorizationHeader));
    }

    @PutMapping("/{productCode}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productCode, @RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.updateProduct(productCode, dto));
    }

    @PutMapping("/delete/{productCode}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productCode) {
        productService.deleteProduct(productCode);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{productCode}")
    public ResponseEntity<?> getProduct(@PathVariable Long productCode) {
        return ResponseEntity.ok(productService.getProduct(productCode));
    }

    @GetMapping("/all")
    public ResponseEntity<PageDto<List<ProductResponse>>> getAllProducts(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                         @RequestParam(value = "size", defaultValue = "15") int size) {
        return ResponseEntity.ok(productService.getAllProducts(page, size));
    }

    @GetMapping("/hello")
    public ResponseEntity<PageDto<List<ProductResponse>>> getAllProductsHello(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                         @RequestParam(value = "size", defaultValue = "15") int size) {
        return ResponseEntity.ok(productService.getAllProducts(page, size));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam(required = false) String name,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) Double minPrice,
                                            @RequestParam(required = false) Double maxPrice,
                                            @RequestParam(required = false) String colour,
                                            @RequestParam(required = false) Double minRating,
                                            @RequestParam(required = false) Double maxRating,
                                            @RequestParam(required = false) Boolean isFeatured,
                                            @RequestParam(required = false) Boolean isDiscounted,
                                            @RequestParam(value = "page", defaultValue = "1") int page, //value = page
                                            @RequestParam(value = "size", defaultValue = "15") int size) {
        return ResponseEntity.ok(productService.filterProducts(name, category, minPrice, maxPrice, colour, minRating, maxRating, isFeatured, isDiscounted, page, size));
    }

    @PutMapping("/move")
    public ResponseEntity<?> move(@RequestParam Long firstCode, @RequestParam Long secondCode) {
        productService.move(firstCode, secondCode);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{productCode}/images")
    public ResponseEntity<List<String>> uploadImages(
            @PathVariable Long productCode,
            @RequestPart("images") List<MultipartFile> images) {

        // Upload images and get their URLs
        List<String> imageUrls = imageUploadService.uploadImages(productCode, images);
        if (imageUrls.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.ok(imageUrls);
    }
}