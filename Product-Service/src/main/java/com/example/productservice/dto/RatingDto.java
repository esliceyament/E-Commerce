package com.example.productservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RatingDto {
    @NotBlank
    private String productId;
    @Min(value = 1, message = "The rating should be at least 1!")
    @Max(value = 5, message = "The maximum rating is 5!")
    private Double rating;
    private String comment;
}
