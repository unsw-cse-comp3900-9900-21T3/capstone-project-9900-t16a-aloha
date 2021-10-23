package com.example.test.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "wishlist")
public class Wishlist {

    @EmbeddedId
    private WishlistId  wishlistId;

    @Column(name = "addtime", nullable = false)
    private Date addTime;

    // default constructor
    public Wishlist() {}

    public WishlistId getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(WishlistId wishlistId) {
        this.wishlistId = wishlistId;
    }

    public Date getAddtime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
