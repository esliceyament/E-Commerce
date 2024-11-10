package com.esliceyament.orderservice.service.cache;

import com.esliceyament.orderservice.response.CartResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
@RequiredArgsConstructor
public class CartCacheService {
    private final JedisPool jedisPool;
    private final ObjectMapper mapper;

    private static final String CART_CACHE_PREFIX_KEY = "cart";
    private static final int CACHE_TTL_SECONDS = 300;
/////jedispool.getresource

    public void cacheCart(String username, CartResponse cart) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = mapper.writeValueAsString(cart);
            jedis.setex(CART_CACHE_PREFIX_KEY + ":" + username, CACHE_TTL_SECONDS, jsonProduct);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public CartResponse getCacheCart(String username) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = jedis.get(CART_CACHE_PREFIX_KEY + ":" + username);
            if (jsonProduct != null) {
                return mapper.readValue(jsonProduct, CartResponse.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } return null;
    }

}
