package com.example.restservice;

public class ChartOrder {
    // instance fields
        double price;
        int quantity;

        public ChartOrder(double price, int quantity) {
            this.price = price;
            this.quantity = quantity;
        }
        public double getPrice() {
            return price;
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
}
