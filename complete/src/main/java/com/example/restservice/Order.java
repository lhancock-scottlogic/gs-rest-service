package com.example.restservice;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class Order {
    // instance fields
    @Min(0)
    @NotNull
    int userId;

    @Size(min=2,max=30)
    @NotNull
    String username;

    @DecimalMin("0.01")
    @NotNull
    double price;

    @Min(1)
    @NotNull
    int quantity;

    @NotNull
    String action;

    @Past
    @NotNull
    LocalDateTime orderDate;

    public Order(int userId, String username, double price, int quantity, String action, LocalDateTime orderDate) {
        this.userId = userId;
        this.username = username;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
        this.orderDate = orderDate;
    }

    public Order(Order order) { // second constructor to allow cloning/deep copies
        this(order.getUserId(), order.getUsername(), order.getPrice(), order.getQuantity(), order.getAction(), order.getOrderDate());
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

    @Override
    public String toString() {
        String orderString = "\nOrder string:\nuserId: " + this.userId + "\nusername: " + this.username + "\nprice: " + this.price + "\nquantity: " + this.quantity + "\naction: " + this.action + "\ndate: " + this.orderDate;
        return orderString;
    }
}
