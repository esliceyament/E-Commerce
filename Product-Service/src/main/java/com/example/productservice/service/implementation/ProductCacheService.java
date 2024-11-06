package com.example.productservice.service.implementation;

import com.example.productservice.dto.PageDto;
import com.example.productservice.dto.ProductDto;
import com.example.productservice.repository.ProductFilterRepository;
import com.example.productservice.response.ProductResponse;
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
public class ProductCacheService {

    private final JedisPool jedisPool;
    private final ObjectMapper mapper;
    private final ProductFilterRepository filterRepository;

    private static final int CACHE_TTL_SECONDS = 30;

    private static final String PRODUCT_CACHE_KEY_PREFIX = "product:";

    public void cacheProduct(Long productCode, ProductDto productDto) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = mapper.writeValueAsString(productDto);
            jedis.setex(PRODUCT_CACHE_KEY_PREFIX + productCode, CACHE_TTL_SECONDS, jsonProduct);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void cacheFilteredProducts(String name, String category, Double minPrice,
                                      Double maxPrice, String colour, Double minRating, Double maxRating,
                                      Boolean isFeatured, Boolean isDiscounted, int page, int size) {
        String cacheKey = buildCacheKey(name, category, minPrice, maxPrice, colour, minRating, maxRating, isFeatured, isDiscounted, page, size);
        PageDto<List<ProductResponse>> result = filterRepository.findProductsByFilter(name, category, minPrice, maxPrice, colour, minRating, maxRating, isFeatured, isDiscounted, page, size);
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = mapper.writeValueAsString(result);
            jedis.setex(cacheKey, CACHE_TTL_SECONDS, jsonProduct);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String buildCacheKey(String name, String category, Double minPrice,
                                      Double maxPrice, String colour, Double minRating, Double maxRating,
                                      Boolean isFeatured, Boolean isDiscounted, int page, int size) {
        return PRODUCT_CACHE_KEY_PREFIX + "_" + name + "_" + category + "_" + minPrice + "_" + maxPrice + "_" + colour + "_"
                + minRating + "_" + maxRating + "_" + isFeatured + "_" + isDiscounted + "_" + page + "_" + size;
    }

    public ProductDto getCachedProduct(Long productCode) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = jedis.get(PRODUCT_CACHE_KEY_PREFIX + productCode);
            if (jsonProduct != null) {
                return mapper.readValue(jsonProduct, ProductDto.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public PageDto<List<ProductResponse>> getCachedFilteredProducts(String name, String category, Double minPrice,
                                                         Double maxPrice, String colour, Double minRating, Double maxRating,
                                                         Boolean isFeatured, Boolean isDiscounted, int page, int size) {
        String cacheKey = buildCacheKey(name, category, minPrice, maxPrice, colour, minRating, maxRating, isFeatured, isDiscounted, page, size);
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = jedis.get(cacheKey);
            if (jsonProduct != null) {
                return mapper.readValue(jsonProduct, new TypeReference<PageDto<List<ProductResponse>>>() {});
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    public void evictProduct(Long productCode) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(PRODUCT_CACHE_KEY_PREFIX + productCode);
        }
    }
}

