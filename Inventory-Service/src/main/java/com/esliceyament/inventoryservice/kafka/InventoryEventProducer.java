package com.esliceyament.inventoryservice.kafka;


import com.esliceyament.shared.payload.StockUpdatePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryEventProducer {

    private final KafkaTemplate<String, StockUpdatePayload> kafkaTemplate;

    public void sendStockUpdateEvent(StockUpdatePayload payload) {
        kafkaTemplate.send("inventory-update-events", payload);
    }
}
