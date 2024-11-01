package com.example.productservice.feign;

import com.example.productservice.dto.category.AttributesPayload;
import com.example.productservice.dto.category.Category;
//import com.example.productservice.dto.category.ProductAttribute;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "Category-Service")
public interface CategoryFeignClient {

    @GetMapping("/categories/id/{id}")
    ResponseEntity<?> getCategory(@PathVariable Long id);

    @GetMapping("/categories/name/{name}")
    ResponseEntity<Category> getCategoryByName(@PathVariable String name);

    @GetMapping("/categories/parent")
    ResponseEntity<?> getAllParentCategories();

    @GetMapping("/categories/sub/{name}")
    ResponseEntity<?> getAllSubcategories(@PathVariable String name);

    @PostMapping("/attributes/adds")
    ResponseEntity<?> createAttributes(@RequestBody List<AttributesPayload> payloads);

}
