package com.example.authSecurity.kafka;

import com.esliceyament.shared.payload.ShippingAddressUpdate;
import com.example.authSecurity.service.implementation.UserProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressEventListener {
    private final UserProfileServiceImpl service;

    @KafkaListener(topics = "address-events", groupId = "auth-group")
    public void handleShippingAddressEvent(ShippingAddressUpdate address) {
        service.addShippingAddress(address);
    }
}
