package com.example.productservice.kafka;

import com.esliceyament.shared.payload.StockUpdatePayload;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockUpdateListener {

    private final ProductRepository repository;

    @KafkaListener(topics = "inventory-update-events", groupId = "product-group")
    public void handleStockUpdateEvent(StockUpdatePayload payload) {
        Product product = repository.findByProductCode(payload.getProductCode())
                .orElseThrow(() -> new NotFoundException("Product with code " + payload.getProductCode() + " not found!"));
        product.setTotalStock(payload.getTotalStock());
        repository.save(product);
    }
}
