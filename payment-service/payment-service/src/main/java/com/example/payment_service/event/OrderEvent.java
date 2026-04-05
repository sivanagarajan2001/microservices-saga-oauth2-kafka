package com.example.payment_service.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderEvent {

    private String eventType;
    private String orderId;
    private double amount;

    public OrderEvent() {}

    public OrderEvent(String eventType, String orderId, double amount) {
        this.eventType = eventType;
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
