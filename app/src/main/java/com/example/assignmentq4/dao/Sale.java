package com.example.assignmentq4.dao;

public class Sale {
    public int id;
    public int productId;
    public int quantity;
    public String date;
    public double totalPrice;

    public Sale(int id, int productId, int quantity, String date, double totalPrice) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.date = date;
        this.totalPrice = totalPrice;
    }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}