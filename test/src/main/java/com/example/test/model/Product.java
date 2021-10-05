package com.example.test.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

// ANNOTATION: maybe it is a good idea to change the table names to "products"
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "productid")
    private String id;

    @Column(name = "lastvisitedtime", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastVisitedTime;

    @Column(name = "avgrating")
    private float avgRating;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private float price;

    @Column(name = "discount")
    private float discount;

    @Column(name = "brand")
    private String brand;

    @Column(name = "description")
    private String description;

    @Column(name = "imgurl")
    private String imgURL;

    @Column(name = "visibility")
    private int visibility;

    // ANNOTATION: newly add one to many relationship with storge table
    @OneToMany(mappedBy = "storgeid.product")
    private Set<Storge> storge;

    public Product() {

    }

    // ANNOTATION
    // OPTIONAL: create constructor with argument

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getLastVisitedTime() {
        return lastVisitedTime;
    }

    public void setLastVisitedTime(Date lastVisitedTime) {
        this.lastVisitedTime = lastVisitedTime;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
}
