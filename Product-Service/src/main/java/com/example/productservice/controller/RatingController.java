package com.example.productservice.controller;

import com.example.productservice.dto.RatingDto;
import com.example.productservice.service.implementation.RatingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {
    private final RatingServiceImpl service;

    @PostMapping("/add")
    public ResponseEntity<?> addRating(@RequestBody RatingDto dto, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return ResponseEntity.ok(service.addRating(dto, authorizationHeader));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRating(@PathVariable String id, @RequestBody RatingDto dto) {
        return ResponseEntity.ok(service.updateRating(id, dto));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable String id) {
        service.deleteRating(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRating(@PathVariable String id) {
        return ResponseEntity.ok(service.getCachedRating(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllRatings() {
        return ResponseEntity.ok(service.getAllCachedRatings());
    }

}
