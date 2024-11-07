package com.esliceyament.orderservice.service;

import com.esliceyament.orderservice.response.OrderResponse;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {

    OrderResponse placeOrder(String authorizationHeader, Long addressId);

}
