package com.esliceyament.orderservice.service;

import com.esliceyament.orderservice.response.OrderResponse;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {

    OrderResponse placeOrder(String authorizationHeader, Long addressId);
    OrderResponse updateOrder(Long id, OrderStatus status);
    OrderResponse getOrder(Long id, String authorizationHeader);
    OrderHistoryResponse getOrderHistory(Long id, String authorizationHeader);
    List<OrderHistoryResponse> getAllOrderHistories(String authorizationHeader);
    Address updateShippingAddress(Address address, Long id, String authorizationHeader);
    void cancelOrder(Long id, String authorizationHeader);


}
