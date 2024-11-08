package com.esliceyament.orderservice.controller;

import com.esliceyament.orderservice.enums.ReturnStatus;
import com.esliceyament.orderservice.payload.ReturnPayload;
import com.esliceyament.orderservice.service.ReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/return")
@RequiredArgsConstructor
public class ReturnController {

    private final ReturnService returnService;

    @PostMapping("/add/{id}")
    public ResponseEntity<?> returnOrder(@PathVariable Long id, @RequestBody ReturnPayload payload, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return ResponseEntity.ok(returnService.returnOrder(id, payload, authorizationHeader));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReturnRequest(@PathVariable Long id, @RequestParam ReturnStatus status) {
        return ResponseEntity.ok(returnService.updateReturnRequest(id, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReturnRequest(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return ResponseEntity.ok(returnService.getReturnRequest(id, authorizationHeader));
    }

    @GetMapping
    public ResponseEntity<?> getAllReturnRequests(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return ResponseEntity.ok(returnService.getAllReturnRequests(authorizationHeader));
    }

}
