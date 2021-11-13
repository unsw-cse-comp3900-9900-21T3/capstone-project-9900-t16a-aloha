package com.example.test.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;


// class that is required to make composite key for shoppingCart
@Embeddable
public class ShoppingCartId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "productid", nullable = false)
    private Product product;

    @Column(name = "size", nullable = false)
    private Float size;

    //default constructor as required for composite key
    public ShoppingCartId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartId that = (ShoppingCartId) o;
        return user.equals(that.user) && product.equals(that.product) && size.equals(that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, product, size);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
