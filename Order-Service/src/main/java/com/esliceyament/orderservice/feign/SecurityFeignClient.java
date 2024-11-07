package com.esliceyament.orderservice.feign;

import com.esliceyament.orderservice.entity.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Auth-Security")
public interface SecurityFeignClient {

    @GetMapping("/authenticate/getUsername")
    String getUsername(@RequestHeader("Authorization") String token);

    @GetMapping("/users/profile/get-address")
    Address getAddress(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);

    @GetMapping("/users/profile/get-address-by-id")
    Address getAddressById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, Long id);

    @PutMapping("/users/profile/update-address")
    void updateDefaultAddress(@RequestParam Long addressId);
}
