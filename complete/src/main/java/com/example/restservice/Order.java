package com.example.restservice;
import java.util.Date;

public class Order {
    // instance fields
    int userId;
    String username;
    float price;
    int quantity;
    String action;
    Date orderDate;

    public Order(int userId, String username, float price, int quantity, String action, Date orderDate) {
        this.userId = userId;
        this.username = username;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
        this.orderDate = orderDate;
    }
}
