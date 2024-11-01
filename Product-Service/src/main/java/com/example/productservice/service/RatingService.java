package com.example.productservice.service;

import com.example.productservice.dto.RatingDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RatingService {
    RatingDto addRating(RatingDto dto);
    RatingDto updateRating(String id, RatingDto dto);
    void deleteRating(String id);
    RatingDto getRating(String id);
    List<RatingDto> getAllRatings();
}
