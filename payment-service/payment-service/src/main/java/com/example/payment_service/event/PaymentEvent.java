package com.example.payment_service.event;

public class PaymentEvent {
    private String status;
    private String orderId;
    private double amount;

    public PaymentEvent() {}

    public PaymentEvent(String status, String orderId, double amount) {
        this.status = status;
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
