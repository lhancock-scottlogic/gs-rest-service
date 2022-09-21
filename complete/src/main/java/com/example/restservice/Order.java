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

    public double getPrice() {
        return price;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }
}
