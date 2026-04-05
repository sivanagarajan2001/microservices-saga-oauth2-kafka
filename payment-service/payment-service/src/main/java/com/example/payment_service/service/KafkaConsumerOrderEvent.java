package com.example.payment_service.service;

import com.example.payment_service.event.OrderEvent;
import com.example.payment_service.event.PaymentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerOrderEvent {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "order-events", groupId = "payment-group")
    public void process(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            OrderEvent event = mapper.readValue(message, OrderEvent.class);

            if (Math.random() > 0.5) {

                kafkaTemplate.send("payment-events",
                        new PaymentEvent("PAYMENT_SUCCESS",
                                event.getOrderId(),
                                event.getAmount()));

            } else {
                throw new RuntimeException("Payment failed");
            }

        } catch (Exception e) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                OrderEvent event = mapper.readValue(message, OrderEvent.class);

                kafkaTemplate.send("payment-events",
                        new PaymentEvent("PAYMENT_FAILED",
                                event.getOrderId(),
                                event.getAmount()));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

