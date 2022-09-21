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

    public LocalDateTime getTradeDate() {
        return tradeDate;
    }
}
