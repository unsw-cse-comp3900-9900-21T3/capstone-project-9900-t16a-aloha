package com.example.test.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "recommend")
public class Recommend {
    @Id
    @Column(name = "productid")
    private String id;
    @Column(name = "s1")
    private String s1;
    @Column(name = "s2")
    private String s2;
    @Column(name = "s3")
    private String s3;
    @Column(name = "s4")
    private String s4;
    @Column(name = "s5")
    private String s5;
    @Column(name = "s6")
    private String s6;
    @Column(name = "s7")
    private String s7;
    @Column(name = "s8")
    private String s8;
    @Column(name = "s9")
    private String s9;
    @Column(name = "s10")
    private String s10;

    public Recommend() {};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public String getS3() {
        return s3;
    }

    public void setS3(String s3) {
        this.s3 = s3;
    }

    public String getS4() {
        return s4;
    }

    public void setS4(String s4) {
        this.s4 = s4;
    }

    public String getS5() {
        return s5;
    }

    public void setS5(String s5) {
        this.s5 = s5;
    }

    public String getS6() {
        return s6;
    }

    public void setS6(String s6) {
        this.s6 = s6;
    }

    public String getS7() {
        return s7;
    }

    public void setS7(String s7) {
        this.s7 = s7;
    }

    public String getS8() {
        return s8;
    }

    public void setS8(String s8) {
        this.s8 = s8;
    }

    public String getS9() {
        return s9;
    }

    public void setS9(String s9) {
        this.s9 = s9;
    }

    public String getS10() {
        return s10;
    }

    public void setS10(String s10) {
        this.s10 = s10;
    }
}
