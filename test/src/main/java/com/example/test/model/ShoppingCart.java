package com.example.test.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "shoppingcart")
public class ShoppingCart {

    @EmbeddedId
    private ShoppingCartId shoppingCartId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // default constructor
    public ShoppingCart() {}

    public ShoppingCartId getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(ShoppingCartId shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
