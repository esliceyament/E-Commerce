package com.example.productservice.feign;

import com.example.productservice.payload.InventoryPayload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Inventory-Service")
public interface InventoryFeignClient {

    @PostMapping("/inventory/add")
    ResponseEntity<?> createInventory(@RequestBody InventoryPayload inventory);
}
