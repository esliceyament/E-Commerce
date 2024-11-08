package com.esliceyament.orderservice.kafka;

import com.esliceyament.shared.payload.OrderedStockUpdate;
import com.esliceyament.shared.payload.ShippingAddressUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderedStockUpdate> kafkaTemplate;
    private final KafkaTemplate<String, ShippingAddressUpdate> addressKafkaTemplate;

    public void sendOrderStockUpdate(OrderedStockUpdate orderedStockUpdate) {
        kafkaTemplate.send("order-events", orderedStockUpdate);
    }

    public void sendShippingAddressUpdate(ShippingAddressUpdate address) {
        addressKafkaTemplate.send("address-events", address);
    }
}
