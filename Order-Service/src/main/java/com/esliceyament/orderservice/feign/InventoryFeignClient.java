package com.esliceyament.orderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Inventory-Service")
public interface InventoryFeignClient {

    @GetMapping("/inventory/stock/{productCode}")
    int getStock(@PathVariable Long productCode, @RequestParam(required = false) String value);
}
