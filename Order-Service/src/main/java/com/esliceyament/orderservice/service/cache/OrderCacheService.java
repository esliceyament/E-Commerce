package com.esliceyament.orderservice.service.cache;

import com.esliceyament.orderservice.response.OrderHistoryResponse;
import com.esliceyament.orderservice.response.OrderResponse;
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
public class OrderCacheService {

    private final JedisPool jedisPool;
    private final ObjectMapper mapper;

    private static final String ORDER_CACHE_PREFIX_KEY = "order";
    private static final String ORDER_HISTORY_CACHE_PREFIX_KEY = "order_history";
    private static final int CACHE_TTL_SECONDS = 60;

    public void cacheOrder(Long id, String username, OrderResponse response) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = mapper.writeValueAsString(response);
            jedis.setex(ORDER_CACHE_PREFIX_KEY + ":" + id + ":" + username, CACHE_TTL_SECONDS, jsonProduct);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public OrderResponse getCacheOrder(Long id, String username) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = jedis.get(ORDER_CACHE_PREFIX_KEY + ":" + id + ":" + username);
            if (jsonProduct != null) {
                return mapper.readValue(jsonProduct, OrderResponse.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } return null;
    }

    public void cacheOrderHistory(Long id, String username, OrderHistoryResponse response) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = mapper.writeValueAsString(response);
            jedis.setex(ORDER_HISTORY_CACHE_PREFIX_KEY + ":" + id + ":" + username, CACHE_TTL_SECONDS, jsonProduct);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public OrderHistoryResponse getCacheOrderHistory(Long id, String username) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = jedis.get(ORDER_HISTORY_CACHE_PREFIX_KEY + ":" + id + ":" + username);
            if (jsonProduct != null) {
                return mapper.readValue(jsonProduct, OrderHistoryResponse.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } return null;
    }

    public void cacheAllOrderHistory(String username, List<OrderHistoryResponse> response) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = mapper.writeValueAsString(response);
            jedis.setex(ORDER_HISTORY_CACHE_PREFIX_KEY + ":" + username, CACHE_TTL_SECONDS, jsonProduct);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderHistoryResponse> getAllCacheOrderHistory(String username) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonProduct = jedis.get(ORDER_HISTORY_CACHE_PREFIX_KEY + ":" + username);
            if (jsonProduct != null) {
                return mapper.readValue(jsonProduct, new TypeReference<List<OrderHistoryResponse>>() {});
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } return null;
    }
}
