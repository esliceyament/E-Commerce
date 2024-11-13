package com.example.productservice.service;

import com.example.productservice.dto.PageDto;
import com.example.productservice.dto.ProductDto;
import com.example.productservice.payload.ProductPayload;
import com.example.productservice.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface ProductService {

    ProductDto addProduct(ProductPayload payload, String authorizationHeader);
    ProductDto updateProduct(Long productCode, ProductDto dto);
    void deleteProduct(Long productCode);
    ProductDto getProduct(Long productCode);
    ProductDto getProductCached(Long productCode);
    PageDto<List<ProductResponse>> getAllProducts(int page, int size);
    void move(Long first, Long second);
    PageDto<List<ProductResponse>> filterProducts(String name, String category, Double minPrice,
                                                          Double maxPrice, String colour, Double minRating, Double maxRating,
                                                          Boolean isFeatured, Boolean isDiscounted, int page, int size);
    PageDto<List<ProductResponse>> getCachedFilteredProducts(String name, String category, Double minPrice,
                                                                    Double maxPrice, String colour, Double minRating, Double maxRating,

                                                                    Boolean isFeatured, Boolean isDiscounted, int page, int size);
    Set<ProductDto> getProductsById(Set<Long> ids);
}
