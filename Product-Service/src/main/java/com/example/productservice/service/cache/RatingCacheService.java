package com.example.productservice.service.cache;

import com.example.productservice.dto.RatingDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingCacheService {

    private final JedisPool jedisPool;
    private final ObjectMapper mapper;

    private static final int CACHE_TTL_SECONDS = 100;
    private static final String RATING_CACHE_KEY_PREFIX = "rating";

    public void cacheRating(String id, RatingDto ratingDto) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = mapper.writeValueAsString(ratingDto);
            jedis.setex(RATING_CACHE_KEY_PREFIX + id, CACHE_TTL_SECONDS, jsonProduct);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void cacheAllRatings(List<RatingDto> ratingDtoList) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = mapper.writeValueAsString(ratingDtoList);
            jedis.setex("ratings", 200, jsonProduct);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public RatingDto getCachedRating(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = jedis.get(RATING_CACHE_KEY_PREFIX + id);
            if (jsonProduct != null) {
                return mapper.readValue(jsonProduct, RatingDto.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<RatingDto> getAllCachedRatings() {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = jedis.get("ratings");
            if (jsonProduct != null) {
                return mapper.readValue(jsonProduct, new TypeReference<List<RatingDto>>() {});
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
