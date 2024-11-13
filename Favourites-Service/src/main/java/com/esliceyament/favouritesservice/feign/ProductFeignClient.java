package com.esliceyament.favouritesservice.feign;

import com.esliceyament.favouritesservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(name = "Product-Service")
public interface ProductFeignClient {

    @GetMapping("/products/{productCode}")
    ResponseEntity<ProductDto> getProductCached(@PathVariable Long productCode);

    @PostMapping ("/products/get-products-by-id")
    ResponseEntity<Set<ProductDto>> getProductsById(@RequestBody Set<Long> ids);

    @GetMapping("/products/get-product-by-id")
    ResponseEntity<ProductDto> getProductById(Long id);
}
