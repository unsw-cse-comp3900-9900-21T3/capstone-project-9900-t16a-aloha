package com.example.test.model;

import javax.persistence.*;

@Entity
@Table(name = "storge")
public class Storge {

    @EmbeddedId
    private StorgeId storgeid;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    // default constructor
    public  Storge() {}

    public StorgeId getStorgeid() {
        return storgeid;
    }

    public void setStorgeid(StorgeId storgeid) {
        this.storgeid = storgeid;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
