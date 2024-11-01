package com.esliceyament.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Map;

@Entity
@Data
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productCode;

    @ElementCollection
    Map<String, Integer> attributeStock;

    private int totalStock;
}
