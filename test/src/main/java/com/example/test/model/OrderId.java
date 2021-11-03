package com.example.test.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "orderid")
    private OrderHistory orderHistory;

    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;

    @Column(name = "size")
    private Float size;

    public OrderId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderId that = (OrderId) o;
        return orderHistory.equals(that.orderHistory) && product.equals(that.product) && size.equals(that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderHistory, product, size);
    }

    public OrderHistory getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(OrderHistory orderHistory) {
        this.orderHistory = orderHistory;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }
}
