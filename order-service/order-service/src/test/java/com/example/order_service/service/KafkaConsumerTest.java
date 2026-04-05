package com.example.order_service.service;

import com.example.order_service.model.Order;
import com.example.order_service.model.PaymentEvent;
import com.example.order_service.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class KafkaConsumerTest {

    @Mock
    private OrderRepository repo;

    @InjectMocks
    private KafkaConsumer kafkaConsumer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRollback_Success() throws JsonProcessingException {
        PaymentEvent event = new PaymentEvent();
        event.setOrderId("1");
        event.setStatus("TRANSACTION_SUCCESS");
        String message = mapper.writeValueAsString(event);

        Order order = new Order();
        order.setId(1L);
        order.setStatus("PENDING");

        when(repo.findById(1L)).thenReturn(Optional.of(order));

        kafkaConsumer.rollback(message);

        verify(repo, times(1)).save(order);
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testRollback_Failure() throws JsonProcessingException {
        PaymentEvent event = new PaymentEvent();
        event.setOrderId("1");
        event.setStatus("TRANSACTION_FAILED");
        String message = mapper.writeValueAsString(event);

        Order order = new Order();
        order.setId(1L);
        order.setStatus("PENDING");

        when(repo.findById(1L)).thenReturn(Optional.of(order));

        kafkaConsumer.rollback(message);

        verify(repo, times(1)).save(order);
        assertEquals("PENDING", order.getStatus());
    }
}
