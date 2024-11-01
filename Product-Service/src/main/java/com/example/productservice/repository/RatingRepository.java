package com.example.productservice.repository;

import com.example.productservice.dto.RatingDto;
import com.example.productservice.entity.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, String> {
    List<RatingDto> findAllByStatusTrueOrderByCreateTime();
}
