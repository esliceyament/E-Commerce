package com.esliceyament.orderservice.service;

import com.esliceyament.orderservice.enums.ReturnStatus;
import com.esliceyament.orderservice.payload.ReturnPayload;
import com.esliceyament.orderservice.response.ReturnResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReturnService {

    ReturnResponse returnOrder(Long id, ReturnPayload payload, String authorizationHeader);
    ReturnResponse getReturnRequest(Long id, String authorizationHeader);
    ReturnResponse updateReturnRequest(Long id, ReturnStatus status);
    List<ReturnResponse> getAllReturnRequests(String authorizationHeader);
}
