package com.example.restservice;
import java.time.LocalDateTime;
import java.util.Date;

public class Trade {
    String buyerName;
    String sellerName;
    double price;
    int quantity;
    LocalDateTime tradeDate;

    public Trade(String buyerName, String sellerName, double price, int quantity, LocalDateTime tradeDate) {
        this.buyerName = buyerName;
        this.sellerName = sellerName;
        this.price = price;
        this.quantity = quantity;
        this.tradeDate = tradeDate;
    }
    public String getBuyerName() {
        return buyerName;
    }
    public String getSellerName() {
        return sellerName;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public LocalDateTime getTradeDate() {
        return tradeDate;
    }
}
