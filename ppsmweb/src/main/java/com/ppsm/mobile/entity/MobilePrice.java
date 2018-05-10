package com.ppsm.mobile.entity;

import java.util.Date;

public class MobilePrice {

    private int id;
    private int productId;
    private String price;
    private String time;
    private Date date;
    private int ispublish;

    private MobileProduct mobileProduct;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIspublish() {
        return ispublish;
    }

    public void setIspublish(int ispublish) {
        this.ispublish = ispublish;
    }

    public MobileProduct getMobileProduct() {
        return mobileProduct;
    }

    public void setMobileProduct(MobileProduct mobileProduct) {
        this.mobileProduct = mobileProduct;
    }
}
