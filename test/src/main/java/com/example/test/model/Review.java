package com.example.test.model;

import javax.persistence.*;

@Entity
@Table(name = "review")
public class Review {
    @GeneratedValue
    @Id
    @Column(name = "reviewid")
    private Integer reviewId;

    @OneToOne
    @JoinColumn(name = "orderid", nullable = false)
    private OrderHistory orderHistory;

    @ManyToOne
    @JoinColumn(name = "productid", nullable = false)
    private Product product;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "size")
    private Float size;

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
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

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }
}
