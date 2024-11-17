package com.esliceyament.orderservice.service.implementation;

import com.esliceyament.orderservice.entity.Order;
import com.esliceyament.orderservice.entity.ReturnRequest;
import com.esliceyament.orderservice.enums.OrderStatus;
import com.esliceyament.orderservice.enums.ReturnStatus;
import com.esliceyament.orderservice.exception.NotFoundException;
import com.esliceyament.orderservice.exception.ReturnRequestNotAllowed;
import com.esliceyament.orderservice.feign.SecurityFeignClient;
import com.esliceyament.orderservice.mapper.ReturnMapper;
import com.esliceyament.orderservice.payload.ReturnPayload;
import com.esliceyament.orderservice.repository.OrderRepository;
import com.esliceyament.orderservice.repository.ReturnRepository;
import com.esliceyament.orderservice.response.ReturnResponse;
import com.esliceyament.orderservice.service.ReturnService;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReturnServiceImpl implements ReturnService {
    private final OrderRepository orderRepository;
    private final ReturnRepository repository;
    private final SecurityFeignClient securityFeignClient;
    private final ReturnMapper mapper;

    @Override
    public ReturnResponse returnOrder(Long id, ReturnPayload payload, String authorizationHeader) {
        String username = securityFeignClient.getUsername(authorizationHeader);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found " + id));
        if (!username.equals(order.getBuyerName())) {
            throw new BadRequestException();
        }
        if (!order.getStatus().equals(OrderStatus.SHIPPED) && !order.getStatus().equals(OrderStatus.DELIVERED)) {
            throw new ReturnRequestNotAllowed("You can't return it for now");
        }
        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setReason(payload.getReason());
        returnRequest.setOrderId(id);
        returnRequest.setBuyerName(username);
        returnRequest.setStatus(ReturnStatus.PENDING);
        returnRequest.setRequestDate(LocalDateTime.now());

        repository.save(returnRequest);

        return mapper.toResponse(returnRequest);
    }

    @Override
    public ReturnResponse getReturnRequest(Long id, String authorizationHeader) {
        String username = securityFeignClient.getUsername(authorizationHeader);
        ReturnRequest request = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cannot find"));
        if (!request.getBuyerName().equals(username)) {
            throw new BadRequestException();
        }
        return mapper.toResponse(request);
    }

    @Override
    public ReturnResponse updateReturnRequest(Long id, ReturnStatus status) {
        ReturnRequest request = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Request not found!"));
        request.setStatus(status);
        if (status.equals(ReturnStatus.APPROVED)) {
            request.setApprovalDate(LocalDateTime.now());
        }
        repository.save(request);
        return mapper.toResponse(request);
    }

    @Override
    public List<ReturnResponse> getAllReturnRequests(String authorizationHeader) {
        String username = securityFeignClient.getUsername(authorizationHeader);
        return repository.findAllByBuyerName(username).stream()
                .map(mapper::toResponse).collect(Collectors.toList());
    }
}
