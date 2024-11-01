package com.esliceyament.categoryservice.controller;

import com.esliceyament.categoryservice.entity.Category;
import com.esliceyament.categoryservice.payload.ParentCategoryPayload;
import com.esliceyament.categoryservice.payload.SubcategoryPayload;
import com.esliceyament.categoryservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add/parent")
    public ResponseEntity<?> createParentCategory(@RequestBody ParentCategoryPayload payload) {
        return ResponseEntity.ok(categoryService.createParentCategory(payload));
    }

    @PostMapping("/add/sub")
    public ResponseEntity<?> createSubcategory(@RequestBody SubcategoryPayload payload) {
        return ResponseEntity.ok(categoryService.createSubcategory(payload));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getCategory(@PathVariable Long id) {
        Category category = categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
        Category category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/parent")
    public ResponseEntity<?> getAllParentCategories() {
        return ResponseEntity.ok(categoryService.getAllParentCategories());
    }

    @GetMapping("/sub/{name}")
    public ResponseEntity<?> getAllSubcategories(@PathVariable String name) {
        return ResponseEntity.ok(categoryService.getAllSubcategories(name));
    }

}
