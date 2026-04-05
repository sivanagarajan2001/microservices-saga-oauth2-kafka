package com.example.order_service.service;

import com.example.order_service.model.Order;
import com.example.order_service.model.OrderEvent;
import com.example.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repo;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public Order createOrder(Double amount) {

        Order order = new Order();
        order.setAmount(amount);
//        order.setStatus("PENDING");

        order = repo.save(order);

        try {
            kafkaTemplate.send("order-events",
                            new OrderEvent("ORDER_CREATED",
                                    order.getId().toString(),
                                    amount))
                    ;


        } catch (Exception e) {
            order.setStatus("FAILED");
        }

        order = repo.save(order);

        return order;
    }
}
