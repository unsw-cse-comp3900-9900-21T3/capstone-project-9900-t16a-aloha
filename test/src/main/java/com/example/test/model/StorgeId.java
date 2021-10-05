package com.example.test.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

// class that is required to make composite key happen
@Embeddable
public class StorgeId  implements Serializable {
    @ManyToOne
    @JoinColumn(name = "productid", nullable = false)
    private Product product;

    @Column(name = "size", nullable = false)
    private Float size;

    // default constructor as required for composite key
    public StorgeId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorgeId storgeId = (StorgeId) o;
        return product.equals(storgeId.product) && size.equals(storgeId.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, size);
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
