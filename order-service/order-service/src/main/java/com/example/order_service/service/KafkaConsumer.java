package com.example.order_service.service;

import com.example.order_service.model.Order;
import com.example.order_service.model.PaymentEvent;
import com.example.order_service.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final OrderRepository repo;

    @KafkaListener(topics = "transaction-events", groupId = "order-group")
    public void rollback(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PaymentEvent event = mapper.readValue(message, PaymentEvent.class);

            Order order = repo.findById(Long.parseLong(event.getOrderId())).orElse(null);

            if (order == null) return;

            if ("TRANSACTION_SUCCESS".equals(event.getStatus())) {
                order.setStatus("SUCCESS");
            }

            if ("TRANSACTION_FAILED".equals(event.getStatus())) {
                order.setStatus("PENDING");
            }

            repo.save(order);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
