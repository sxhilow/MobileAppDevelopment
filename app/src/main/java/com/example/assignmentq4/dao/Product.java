package com.example.assignmentq4.dao;

public class Product {
    public int id;
    public String name;
    public double price;
    public double cost;
    public int stock;
    public String category;

    public Product(int id, String name, double price, double cost, int stock, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.stock = stock;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}