package com.esliceyament.orderservice.service.implementation;

import com.esliceyament.orderservice.entity.*;
import com.esliceyament.orderservice.enums.OrderStatus;
import com.esliceyament.orderservice.exception.AddressNotProvidedException;
import com.esliceyament.orderservice.exception.NotFoundException;
import com.esliceyament.orderservice.exception.OrderAlreadyCanceledException;
import com.esliceyament.orderservice.exception.OrderCancellationNotAllowedException;
import com.esliceyament.orderservice.feign.SecurityFeignClient;
import com.esliceyament.orderservice.kafka.OrderEventProducer;
import com.esliceyament.orderservice.mapper.CartItemMapper;
import com.esliceyament.orderservice.mapper.OrderHistoryMapper;
import com.esliceyament.orderservice.repository.CartRepository;
import com.esliceyament.orderservice.repository.ItemRepository;
import com.esliceyament.orderservice.repository.OrderHistoryRepository;
import com.esliceyament.orderservice.repository.OrderRepository;
import com.esliceyament.orderservice.response.OrderHistoryResponse;
import com.esliceyament.orderservice.response.OrderResponse;
import com.esliceyament.orderservice.service.OrderService;
import com.esliceyament.shared.payload.NotificationDto;
import com.esliceyament.shared.payload.OrderedStockUpdate;
import com.esliceyament.shared.payload.ShippingAddressUpdate;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final SecurityFeignClient securityFeignClient;
    private final CartRepository cartRepository;
    private final OrderEventProducer eventProducer;
    private final CartItemMapper mapper;
    private final OrderHistoryMapper historyMapper;
    private final OrderHistoryRepository historyRepository;
    private final ItemRepository itemRepository;
    private final OrderEventProducer producer;


    @Override
    public OrderResponse placeOrder(String authorizationHeader, Long addressId) {
        String username = securityFeignClient.getUsername(authorizationHeader);
        Address selectedAddress;
        if (addressId != null) {
            selectedAddress = securityFeignClient.getAddressById(authorizationHeader, addressId);
            if (selectedAddress == null) {
                throw new NotFoundException("Address not found for the provided address ID.");
            }
            securityFeignClient.updateDefaultAddress(addressId);
        } else {
            selectedAddress = securityFeignClient.getAddress(authorizationHeader);
            if (selectedAddress == null) {
                throw new AddressNotProvidedException("Please provide an address!");
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
        cart.getCartItems().forEach((item -> item.setOrder(order)));
        order.getCartItemSet().addAll(cart.getCartItems());
        order.setShippingAddress(selectedAddress);
        orderRepository.save(order);

        cart.getCartItems().clear();
        cart.setTotalPrice(0D);
        cart.setDiscountPrice(0D);
        cart.setDiscountCode(null);
        cart.setCreatedAt(LocalDateTime.now());
        cartRepository.save(cart);

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

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setUsername(username);
        notificationDto.setSubject("Order placed!");
        notificationDto.setBody("Order " + order.getId() + " has been successfully placed! Total price is " + order.getTotalAmount() + "$");
        eventProducer.sendOrderNotification(notificationDto);

        return orderResponse;

    }

    @Override
    public OrderResponse updateOrder(Long id, OrderStatus status) {
        CartItem cartItem = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found!"));

        Order order = orderRepository.findById(cartItem.getOrder().getId())
                .orElseThrow(() -> new NotFoundException("Cart is not found!"));
        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new OrderAlreadyCanceledException("The order was canceled");
        }
        cartItem.setStatus(status);
        if (status == OrderStatus.CONFIRMED) {
            cartItem.setStageStatus(1);
        } else if (status == OrderStatus.SHIPPED) {
            cartItem.setStageStatus(2);
        } else {
            cartItem.setStageStatus(3);
        }
        itemRepository.save(cartItem);

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setUsername(order.getBuyerName());
        notificationDto.setSubject("Item status!");
        notificationDto.setBody("Item you ordered has been " + status.toString());
        eventProducer.sendOrderNotification(notificationDto);

        if (order.getCartItemSet().stream().allMatch(item -> item.getStageStatus() == 3)) {
            order.setStatus(OrderStatus.DELIVERED);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);
        } else if (order.getCartItemSet().stream().allMatch(item -> item.getStageStatus() >= 2)) {
            order.setStatus(OrderStatus.SHIPPED);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);
        } else if (order.getCartItemSet().stream().allMatch(item -> item.getStageStatus() >= 1)) {
            order.setStatus(OrderStatus.CONFIRMED);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);
        }

        OrderResponse response = new OrderResponse();
        response.setBuyerName(order.getBuyerName());
        response.setOrderedAt(order.getOrderedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        response.setStatus(status);
        response.setTotalAmount(order.getTotalAmount());
        response.setCartItemSet(order.getCartItemSet().stream().map(mapper::toResponse).collect(Collectors.toSet()));
        response.setPaymentId(order.getPaymentId());
        response.setShippingAddress(order.getShippingAddress());

        return response;
    }

    @Override
    public OrderResponse getOrder(Long id, String authorizationHeader) {
        String username = securityFeignClient.getUsername(authorizationHeader);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found " + id));
        if (!username.equals(order.getBuyerName())) {
            throw new BadRequestException();
        }
        OrderResponse response = new OrderResponse();
        response.setBuyerName(order.getBuyerName());
        response.setOrderedAt(order.getOrderedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setCartItemSet(order.getCartItemSet().stream()
                .map(mapper::toResponse).collect(Collectors.toSet()));
        response.setPaymentId(order.getPaymentId());
        response.setShippingAddress(order.getShippingAddress());
        return response;
    }

    @Override
    public OrderHistoryResponse getOrderHistory(Long id, String authorizationHeader) {
        String username = securityFeignClient.getUsername(authorizationHeader);

        OrderHistory history = historyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No history"));
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
                .map(historyMapper::toResponse).toList();
    }

    @Override
    public Address updateShippingAddress(Address address, Long id, String authorizationHeader) {
        String username = securityFeignClient.getUsername(authorizationHeader);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found " + id));

        if (!username.equals(order.getBuyerName())) {
            throw new BadRequestException();
        }

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new AddressNotProvidedException("You can't change address");
        }

        order.setShippingAddress(address);

        orderRepository.save(order);

        ShippingAddressUpdate shippingAddressUpdate = new ShippingAddressUpdate();
        shippingAddressUpdate.setStreet(address.getStreet());
        shippingAddressUpdate.setCity(address.getCity());
        shippingAddressUpdate.setPostalCode(address.getPostalCode());
        shippingAddressUpdate.setCountry(address.getCountry());
        shippingAddressUpdate.setUsername(username);
        producer.sendShippingAddressUpdate(shippingAddressUpdate);

        return address;
    }

    @Override
    public void cancelOrder(Long id, String authorizationHeader) {
        String username = securityFeignClient.getUsername(authorizationHeader);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found " + id));
        if (!username.equals(order.getBuyerName())) {
            throw new BadRequestException();
        }
        if (!order.getStatus().equals(OrderStatus.PENDING) && !order.getStatus().equals(OrderStatus.CONFIRMED)) {
            throw new OrderCancellationNotAllowedException("You can't cancel order");
        }
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setUsername(order.getBuyerName());
        notificationDto.setSubject("Cancellation!");
        notificationDto.setBody("Order " + order.getId() + " has been canceled!");
        eventProducer.sendOrderNotification(notificationDto);
    }

    private void archiveHistory(Order order) {
        OrderHistory history = new OrderHistory();
        history.setOrderId(order.getId());
        history.setUsername(order.getBuyerName());
        history.setOrderDate(order.getOrderedAt());
        history.setStatus(order.getStatus());
        history.setTotalAmount(order.getTotalAmount());
        historyRepository.save(history);
    }
}
