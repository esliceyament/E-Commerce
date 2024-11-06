package com.esliceyament.orderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "Auth-Security")
public interface SecurityFeignClient {

    @GetMapping("/authenticate/getUsername")
    String getUsername(@RequestHeader("Authorization") String token);
}
