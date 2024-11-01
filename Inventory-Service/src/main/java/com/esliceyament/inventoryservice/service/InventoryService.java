package com.esliceyament.inventoryservice.service;

import com.esliceyament.inventoryservice.entity.Inventory;
import com.esliceyament.inventoryservice.kafka.InventoryEventProducer;
import com.esliceyament.inventoryservice.payload.InventoryPayload;
import com.esliceyament.inventoryservice.repository.InventoryRepository;
import com.esliceyament.shared.payload.StockUpdatePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryEventProducer inventoryEventProducer;


    public void createInventory(InventoryPayload payload) {
        if (payload.getAttributeStock() != null) {
            int totalStock = payload.getAttributeStock().values().stream()
                    .mapToInt(Integer::intValue)
                    .sum();
            payload.setTotalStock(totalStock);
        }
        Inventory inventory = new Inventory();
        inventory.setProductCode(payload.getProductCode());
        inventory.setAttributeStock(payload.getAttributeStock());
        inventory.setTotalStock(payload.getTotalStock());
        inventoryRepository.save(inventory);
    }
    public Inventory getInventory(Long productCode) {
        return inventoryRepository.findByProductCode(productCode)
                .orElseThrow(() -> new IllegalStateException("Not found"));
    }
    public int getStock(Long productCode, String value) {
        Inventory inventory = inventoryRepository.findByProductCode(productCode)
                .orElseThrow(() -> new IllegalStateException("Not found"));

        if (value != null) {
            return inventory.getAttributeStock().getOrDefault(value, 0);
        }
        else {
            return inventory.getTotalStock();
        }
    }

    @Transactional
    public void updateStock(Long productCode, String value, int quantity) {
        Inventory inventory = inventoryRepository.findByProductCode(productCode)
                .orElseThrow(() -> new IllegalStateException("Not found"));

        if (value != null && inventory.getAttributeStock().containsKey(value)) {
            int newStock = inventory.getAttributeStock().get(value) - quantity;
            inventory.setTotalStock(inventory.getTotalStock() - quantity);
            if (newStock >= 0) {
                inventory.getAttributeStock().put(value, newStock);
            } else {
                throw new IllegalStateException("Not found");
            }
        }
        else {
            if (inventory.getTotalStock() >= quantity) {
                inventory.setTotalStock(inventory.getTotalStock() - quantity);
            } else {
                throw new IllegalStateException("Not found");
            }
        }
        inventoryRepository.save(inventory);

        StockUpdatePayload stockUpdatePayload = new StockUpdatePayload();
        stockUpdatePayload.setProductCode(productCode);
        stockUpdatePayload.setTotalStock(inventory.getTotalStock());
        inventoryEventProducer.sendStockUpdateEvent(stockUpdatePayload);
    }
    
}
