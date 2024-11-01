package com.esliceyament.inventoryservice.payload;

import lombok.Data;

import java.util.Map;

@Data
public class InventoryPayload {
    private Long productCode;

    Map<String, Integer> attributeStock;

    private int totalStock;
}

