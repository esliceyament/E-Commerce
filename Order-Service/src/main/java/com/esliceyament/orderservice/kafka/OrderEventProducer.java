package com.esliceyament.orderservice.kafka;

import com.esliceyament.shared.payload.OrderedStockUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderedStockUpdate> kafkaTemplate;

    public void sendOrderStockUpdate(OrderedStockUpdate orderedStockUpdate) {
        kafkaTemplate.send("order-events", orderedStockUpdate);
    }
}
