package com.esliceyament.orderservice.feign;

import com.esliceyament.orderservice.dto.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Product-Service")
public interface ProductFeignClient {

    @GetMapping("/products/{productCode}")
    ResponseEntity<Product> getProduct(@PathVariable Long productCode);
}
