package com.esliceyament.orderservice.service.implementation;

import com.esliceyament.orderservice.entity.Address;
import com.esliceyament.orderservice.entity.Cart;
import com.esliceyament.orderservice.entity.CartItem;
import com.esliceyament.orderservice.entity.Order;
import com.esliceyament.orderservice.enums.OrderStatus;
import com.esliceyament.orderservice.feign.SecurityFeignClient;
import com.esliceyament.orderservice.kafka.OrderEventProducer;
import com.esliceyament.orderservice.mapper.CartItemMapper;
import com.esliceyament.orderservice.repository.CartRepository;
import com.esliceyament.orderservice.repository.OrderRepository;
import com.esliceyament.orderservice.response.OrderResponse;
import com.esliceyament.orderservice.service.OrderService;
import com.esliceyament.shared.payload.OrderedStockUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final SecurityFeignClient securityFeignClient;
    private final CartRepository cartRepository;
    private final OrderEventProducer eventProducer;
    private final CartItemMapper mapper;


    @Override
    public OrderResponse placeOrder(String authorizationHeader, Long addressId) {
        String username = securityFeignClient.getUsername(authorizationHeader);
        Address selectedAddress;
        if (addressId != null) {
            selectedAddress = securityFeignClient.getAddressById(authorizationHeader, addressId);
            if (selectedAddress == null) {
                throw new RuntimeException("Address not found for the provided address ID.");
            }
            securityFeignClient.updateDefaultAddress(addressId);
        } else {
            selectedAddress = securityFeignClient.getAddress(authorizationHeader);
            if (selectedAddress == null) {
                throw new RuntimeException("Please provide ad address!");
            }
        }

        Cart cart = cartRepository.findByBuyerName(username);

        Order order = new Order();
        order.setBuyerName(username);
        order.setOrderedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        if (cart.getDiscountCode() != null) {
            order.setTotalAmount(cart.getDiscountPrice());
            cart.getDiscountCodes().add(cart.getDiscountCode());
        } else {
            order.setTotalAmount(cart.getTotalPrice());
        }
        order.getCartItemSet().addAll(cart.getCartItems());
        order.setShippingAddress(selectedAddress);
        orderRepository.save(order);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setBuyerName(order.getBuyerName());
        orderResponse.setOrderedAt(order.getOrderedAt());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setCartItemSet(order.getCartItemSet().stream()
                .map(mapper::toResponse).collect(Collectors.toSet()));
        orderResponse.setShippingAddress(order.getShippingAddress());

        for (CartItem cartItem : order.getCartItemSet()) {
            OrderedStockUpdate orderedStockUpdate = new OrderedStockUpdate();
            orderedStockUpdate.setProductCode(cartItem.getProductCode());
            orderedStockUpdate.setQuantity(cartItem.getQuantity());
            orderedStockUpdate.setSelectedAttributes(cartItem.getSelectedAttributes());
            eventProducer.sendOrderStockUpdate(orderedStockUpdate);
        }

        archiveHistory(order);

        return orderResponse;

    @Override
    public OrderHistoryResponse getOrderHistory(Long id, String authorizationHeader) {
        String username = securityFeignClient.getUsername(authorizationHeader);
        OrderHistory history = historyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No history"));
        if (!username.equals(history.getUsername())) {
            throw new BadRequestException();
        }
        OrderHistoryResponse response = new OrderHistoryResponse();
        response.setOrderId(history.getOrderId());
        response.setUsername(history.getUsername());
        response.setOrderDate(history.getOrderDate());
        response.setStatus(history.getStatus());
        response.setTotalAmount(history.getTotalAmount());
        return response;
    }

    @Override
    public List<OrderHistoryResponse> getAllOrderHistories(String authorizationHeader) {
        String username = securityFeignClient.getUsername(authorizationHeader);
        return historyRepository.findAllByUsername(username).stream()
                .map(historyMapper::toResponse).collect(Collectors.toList());
    }
    }
}
