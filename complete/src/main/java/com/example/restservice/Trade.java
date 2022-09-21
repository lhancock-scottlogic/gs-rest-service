package com.example.restservice;
import java.util.Date;

public class Trade {
    String buyerName;
    String sellerName;
    float price;
    int quantity;
    Date tradeDate;

    public Trade(String buyerName, String sellerName, float price, int quantity, Date tradeDate) {
        this.buyerName = buyerName;
        this.sellerName = sellerName;
        this.price = price;
        this.quantity = quantity;
        this.tradeDate = tradeDate;
    }
}
