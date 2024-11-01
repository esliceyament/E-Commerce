package com.esliceyament.inventoryservice.controller;

import com.esliceyament.inventoryservice.entity.Inventory;
import com.esliceyament.inventoryservice.payload.InventoryPayload;
import com.esliceyament.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/add")
    public ResponseEntity<?> createInventory(@RequestBody InventoryPayload inventory) {
        inventoryService.createInventory(inventory);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/{productCode}")
    public Inventory getInventory(@PathVariable Long productCode) {
        return inventoryService.getInventory(productCode);
    }

    @GetMapping("/stock/{productCode}")
    public int getStock(@PathVariable Long productCode, @RequestParam(required = false) String value) {
        return inventoryService.getStock(productCode, value);
    }

    @PutMapping("/update/{productCode}")
    public ResponseEntity<?> updateStock(@PathVariable Long productCode,
                                         @RequestParam(required = false) String value,
                                         @RequestParam int quantity) {
        inventoryService.updateStock(productCode, value, quantity);
        return ResponseEntity.ok("Updated");
    }
}
