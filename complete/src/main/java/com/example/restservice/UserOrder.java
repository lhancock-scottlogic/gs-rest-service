package com.example.restservice;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORDERS")
public class UserOrder {
    // instance fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Min(0)
    @NotNull
    int accountId;

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
    String orderAction;

    @Past
    @NotNull
    LocalDateTime orderDate;

    public UserOrder() {
    }

    public UserOrder(int accountId, String username, double price, int quantity, String orderAction, LocalDateTime orderDate) {
        this.accountId = accountId;
        this.username = username;
        this.price = price;
        this.quantity = quantity;
        this.orderAction = orderAction;
        this.orderDate = orderDate;
    }

    public UserOrder(UserOrder order) { // second constructor to allow cloning/deep copies
        this(order.getaccountId(), order.getUsername(), order.getPrice(), order.getQuantity(), order.getOrderAction(), order.getOrderDate());
    }

    public Integer getId() { return id; }

    public int getaccountId() {
        return accountId;
    }

    public void setaccountId(int accountId) {
        this.accountId = accountId;
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

    public String getOrderAction() {
        return orderAction;
    }

    public void setOrderAction(String orderAction) {
        this.orderAction = orderAction;
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
        String orderString = "\nOrder string:\naccountId: " + this.accountId + "\nusername: " + this.username + "\nprice: " + this.price + "\nquantity: " + this.quantity + "\naction: " + this.orderAction + "\ndate: " + this.orderDate;
        return orderString;
    }
}
