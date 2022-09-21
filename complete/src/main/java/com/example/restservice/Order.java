package com.example.restservice;
import java.time.LocalDateTime;
import java.util.Date;

public class Order {
    // instance fields
    int userId;
    String username;
    double price;
    int quantity;
    String action;
    LocalDateTime orderDate;

    public Order(int userId, String username, double price, int quantity, String action, LocalDateTime orderDate) {
        this.userId = userId;
        this.username = username;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
        this.orderDate = orderDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double getPrice() {
        return price;
    }
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
}
