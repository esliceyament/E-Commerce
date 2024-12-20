package com.example.productservice.repository;

import com.example.productservice.dto.PageDto;
import com.example.productservice.entity.Product;
import com.example.productservice.enums.Genders;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class ProductFilterRepository {

    private final MongoTemplate mongoTemplate;
    private final ProductMapper mapper;

    public PageDto<List<ProductResponse>> findProductsByFilter(String name, String category, Double minPrice,
                                                               Double maxPrice, String colour, Double minRating, Double maxRating,
                                                               Boolean isFeatured, Boolean isDiscounted, Genders gender, int page, int size) {
        Query query = new Query();

        if (name != null && !name.isEmpty()) {
            query.addCriteria(Criteria.where("name")
                    .regex("^" + name, "i"));
        }
        if (category != null && !category.isEmpty()) {
            query.addCriteria(Criteria.where("category").is(category));
        }
        if (minPrice != null && maxPrice != null) {
            query.addCriteria(Criteria.where("price").gte(minPrice).lte(maxPrice));
        }
        else if (minPrice != null) {
            query.addCriteria(Criteria.where("price")
                    .gte(minPrice));
        }
        else if (maxPrice != null) {
            query.addCriteria(Criteria.where("price")
                    .lte(maxPrice));
        }
        if (colour != null && !colour.isEmpty()) {
            query.addCriteria(Criteria.where("colour").is(colour));
        }
        if (minRating != null && maxRating != null) {
            query.addCriteria(Criteria.where("rating").gte(minRating).lte(maxRating));
        }
        else if (minRating != null) {
            query.addCriteria(Criteria.where("rating")
                    .gte(minRating));
        }
        else if (maxRating != null) {
            query.addCriteria(Criteria.where("rating")
                    .lte(maxRating));
        }
        if (isFeatured != null) {
            query.addCriteria(Criteria.where("isFeatured").is(isFeatured));
        }
        if (isDiscounted != null) {
            query.addCriteria(Criteria.where("isDiscounted").is(isDiscounted));
        }
        if (gender != null) {
            List<String> genderOptions;
            if (gender.toString().equalsIgnoreCase("MALE")) {
                genderOptions = Arrays.asList("MALE", "UNISEX");
            } else if (gender.toString().equalsIgnoreCase("FEMALE")) {
                genderOptions = Arrays.asList("FEMALE", "UNISEX");
            } else {
                genderOptions = Collections.singletonList(gender.toString().toUpperCase());
            }
            query.addCriteria(Criteria.where("gender").in(genderOptions));
        }

        Pageable pageable = PageRequest.of(page - 1, size);
        query.with(pageable);
        List<Product> products = mongoTemplate.find(query, Product.class);
        long totalElements = products.size();

        List<ProductResponse> productList = products
                .stream().map(mapper::toResponse).toList();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PageDto<>(page, size, totalElements, totalPages, List.of(productList));
    }
}
