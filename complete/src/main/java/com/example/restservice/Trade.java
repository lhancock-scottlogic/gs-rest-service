package com.example.restservice;
import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "TRADES")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String buyerName;
    String sellerName;
    double price;
    int quantity;
    LocalDateTime tradeDate;

    public Trade() {
    }

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
