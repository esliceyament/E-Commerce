package com.esliceyament.inventoryservice.kafka;

import com.esliceyament.inventoryservice.service.InventoryService;
import com.esliceyament.shared.payload.OrderedStockUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventListener {
    private final InventoryService service;

    @KafkaListener(topics = "order-events", groupId = "inventory-group")
    public void handleOrderStockUpdateEvent(OrderedStockUpdate payload) {
        service.updateStock(payload.getProductCode(), payload.getSelectedAttributes(), payload.getQuantity());
    }
}
