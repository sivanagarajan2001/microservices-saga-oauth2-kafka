package com.example.transaction_service;

import com.example.transaction_service.OrderEvent;
import com.example.transaction_service.PaymentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerTransactionEvent {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "payment-events", groupId = "transaction-group")
    public void process(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PaymentEvent event = mapper.readValue(message, PaymentEvent.class);

            if ("PAYMENT_SUCCESS".equals(event.getStatus())) {

                if (Math.random() > 0 ) {

                    kafkaTemplate.send("transaction-events",
                            new PaymentEvent("TRANSACTION_SUCCESS",
                                    event.getOrderId(),
                                    event.getAmount()));

                } else {
                    throw new RuntimeException("Transaction failed");
                }

            } else {
                // payment failed → directly fail transaction
                kafkaTemplate.send("transaction-events",
                        new PaymentEvent("TRANSACTION_FAILED",
                                event.getOrderId(),
                                event.getAmount()));
            }

        } catch (Exception e) {

            try {
                ObjectMapper mapper = new ObjectMapper();
                PaymentEvent event = mapper.readValue(message, PaymentEvent.class);

                kafkaTemplate.send("transaction-events",
                        new PaymentEvent("TRANSACTION_FAILED",
                                event.getOrderId(),
                                event.getAmount()));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    }
