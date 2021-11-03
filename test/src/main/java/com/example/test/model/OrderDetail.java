package com.example.test.model;

import javax.persistence.*;

@Entity
@Table(name = "orderdetail")
public class OrderDetail {
    @EmbeddedId
    private OrderId orderId;

    @Column(name = "quantity")
    private int quantity;

    public OrderDetail() {};

    public OrderId getOrderId() {
        return orderId;
    }

    public void setOrderId(OrderId orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
