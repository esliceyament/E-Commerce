package com.esliceyament.orderservice.kafka;

import com.esliceyament.shared.payload.NotificationDto;
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
    private final KafkaTemplate<String, NotificationDto> notificationKafkaTemplate;

    public void sendOrderStockUpdate(OrderedStockUpdate orderedStockUpdate) {
        kafkaTemplate.send("order-events", orderedStockUpdate);
    }

    public void sendShippingAddressUpdate(ShippingAddressUpdate address) {
        addressKafkaTemplate.send("address-events", address);
    }

    public void sendOrderNotification(NotificationDto dto) {
        notificationKafkaTemplate.send("notification-events", dto);
    }


}
