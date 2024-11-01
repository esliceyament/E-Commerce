package com.example.productservice.service.implementation;

import com.example.productservice.dto.RatingDto;
import com.example.productservice.entity.Product;
import com.example.productservice.entity.Rating;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.RatingMapper;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.RatingRepository;
import com.example.productservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository repository;
    private final ProductRepository productRepository;
    private final RatingMapper mapper;

    public RatingDto addRating(RatingDto dto) {

        Rating rating = mapper.toEntity(dto);
        rating.setCreateTime(LocalDate.now());
        rating.setUserId(1L);
        rating.setStatus(true);
        repository.save(rating);

        Product product = getProduct(rating.getProductId());

        if (product.getRatingIds() == null) {
            product.setRatingIds(new ArrayList<>());
        }
        product.getRatingIds().add(rating.getId());
        product.setRating(avgRating(product.getId(), rating.getRating()));
        product.setRatingCount(ratingCount(product.getId()));
        productRepository.save(product);

        return mapper.toDto(rating);
    }

    @Override
    public RatingDto updateRating(String id, RatingDto dto) {
        Rating rating = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rating not found!"));
        mapper.updateRatingFromDto(dto, rating);
        repository.save(rating);
        return mapper.toDto(rating);
    }

    @Override
    public void deleteRating(String id) {
        Rating rating = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rating not found!"));
        rating.setStatus(false);
        repository.save(rating);
    }

    @Override
    public RatingDto getRating(String id) {
        Rating rating = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rating not found!"));
        return mapper.toDto(rating);
    }

    @Override
    public List<RatingDto> getAllRatings() {
        return repository.findAllByStatusTrueOrderByCreateTime();
    }

    private Double avgRating(String id, Double newRating) {
        Product product = getProduct(id);
        Double rating = product.getRating();
        if (product.getRatingCount() == 0) {
            rating = newRating;
        }
        else {
            rating = (rating * product.getRatingCount() + newRating) / (product.getRatingCount() + 1);
        }
        return rating;
    }

    private Product getProduct(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found!"));
    }

    private Integer ratingCount(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found!"));
        return product.getRatingCount() + 1;
    }

}
