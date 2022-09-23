package com.example.restservice;
import com.example.restservice.Order;
import com.example.restservice.Trade;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
//import java.util.List;

public class Matcher {
    ArrayList<Order> buyList;
    ArrayList<Order> sellList;
    ArrayList<Trade> tradeList;

    // ********** Constructor **********
    public Matcher(ArrayList<Order> buyList, ArrayList<Order> sellList, ArrayList<Trade> tradeList) {
        this.buyList = buyList;
        this.sellList = sellList;
        this.tradeList = tradeList;
    }

    // ********** LIST SORT METHODS **********
    public ArrayList<Order> sortPriceHighToLow(ArrayList<Order> list) { // highest price is at Array[0]
        list.sort(Comparator.comparing(Order::getPrice).reversed());
        return list;
    }
    public ArrayList<Order> sortPriceLowToHigh(ArrayList<Order> list) { // lowest price is at Array[0]
        list.sort(Comparator.comparing(Order::getPrice));
        return list;
    }
    public ArrayList<Order> sortDateHighToLow(ArrayList<Order> list) { // latest date is at Array[0]
        list.sort(Comparator.comparing(Order::getOrderDate).reversed());
        return list;
    }
    public ArrayList<Order> sortDateLowToHigh(ArrayList<Order> list) { // earliest date is at Array[0]
        list.sort(Comparator.comparing(Order::getOrderDate));
        return list;
    }
    public ArrayList<Trade> sortTradeList(ArrayList<Trade> list) { // latest date is at Array[0] - but using Trade object, not Order object
        list.sort(Comparator.comparing(Trade::getTradeDate).reversed());
        return list;
    }

    public int removeOrder(Order orderToRemove) {
        if (Objects.equals(orderToRemove.getAction(), "buy")) {
            if (buyList.contains(orderToRemove)) {
                buyList.remove(orderToRemove);
                return 1;
            }
        }
        if (Objects.equals(orderToRemove.getAction(), "sell")) {
            if (sellList.contains(orderToRemove)) {
                sellList.remove(orderToRemove);
                return 1;
            }
        }
        return 0;
    }

    public Trade createTrade(String buyer, String seller, double price, int quantity, LocalDateTime tradeDate) {
        try {
            Trade newTrade = new Trade(buyer, seller, price, quantity, tradeDate);
            this.tradeList.add(newTrade);
            return newTrade;
        }
        catch(Exception e) {
            System.out.println("Error in createTrade call: " + e + ", returning null.");
            return null;
        }
    }

    // AddOrder: Takes a new order and returns a list of any trades made (result of searchBuyList/searchSellList)
    public ArrayList<Trade> addOrder(Order newOrder) {
        switch(newOrder.action) {
            case "buy":
                this.buyList.add(newOrder);
                this.buyList = sortDateLowToHigh(this.buyList); // sort by earliest added, then by highest price
                this.buyList = sortPriceHighToLow(this.buyList);
                return searchSellList(newOrder); // then return result of searchBuyList
            case "sell":
                this.sellList.add(newOrder);
                this.sellList = sortDateLowToHigh(this.sellList); // sort by earliest added, then by lowest price
                this.sellList = sortPriceLowToHigh(this.sellList);
                return searchBuyList(newOrder); // then return result of searchBuyList
            default:
                System.out.println("Error: action is " + newOrder.action + ", but must be buy or sell.");
                return new ArrayList<>(); // return empty ArrayList if there's an error to prevent frontend collapsing
        }
    }
    // searchBuyList: Takes a new order and returns an ArrayList of any trades made, or an empty list if none
    public ArrayList<Trade> searchBuyList(Order newOrder) {
        ArrayList<Trade> currentTradeList = new ArrayList<Trade>();
        if (this.buyList.size() > 0) {
            for (int i = this.buyList.size() - 1; i >= 0; i--) {
                if ((this.buyList.get(i).getPrice() >= newOrder.getPrice())) {
                    if (this.buyList.get(i).getQuantity() > newOrder.getQuantity()) {
                        this.buyList.get(i).setQuantity(this.buyList.get(i).getQuantity() - newOrder.getQuantity());

                    }
                }
            }
        }
        return currentTradeList;
    }
    // searchSellList: Takes a new order and returns an ArrayList of any trades made, or an empty list if none
    public ArrayList<Trade> searchSellList(Order newOrder) {
        return new ArrayList<Trade>();
    }

    // hasPrice: Takes a list and a price and returns the index of where that price is present in the list
    public int hasPrice(ArrayList<Order> list, double amount) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPrice() == amount) {
                return i;
            }
        }
        return -1; // if nothing was returned by now, it means the price is not in the list
    }

    public ArrayList<Order> aggOrderBook(String action, int numOfOrders, double pricePoint) throws Exception {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.UP);
        ArrayList<Order> list  = new ArrayList<>();
        ArrayList<Order> aggList = new ArrayList<Order>();
        if (Objects.equals(action, "buy")) {
            list = this.buyList;
        }
        else if (Objects.equals(action, "sell")) {
            list = this.sellList;
        }
        else {
            throw new Exception("Error in aggOrderBook call, action is " + action + ", but must be buy or sell");
        }
        double price;
        for (Order order : list) { // run through order objects in the list (enhanced for)
            if (Math.round(order.getPrice() / pricePoint) == 0) {
                price = pricePoint;
            } else {
                price = Math.round(order.getPrice() / pricePoint) * pricePoint;
            }
            aggList.add(new Order(order.getUserId(), order.getUsername(), (Math.round(price * 100.0) / 100.0), order.getQuantity(), order.getAction(), order.getOrderDate()));
        }
        sortDateHighToLow(aggList);
        switch(action) {
            case "buy":
                sortPriceHighToLow(aggList);
                break;
            case "sell":
                sortPriceLowToHigh(aggList);
                break;
        }
        if (numOfOrders == -1) {
            return aggList;
        }
        else if (aggList.size() > numOfOrders) {
            aggList = aggList.subList(0, numOfOrders);
        }
        return aggList;
    }

    public ArrayList<Trade> getAllTrades() {
        sortTradeList(this.tradeList);
        return this.tradeList;
    }

    public ArrayList<Order> getSpecificUserOrders(String username) {
        ArrayList<Order> userOrders = new ArrayList<>();
        for (Order order : this.buyList) {
            if (Objects.equals(order.getUsername(), username)) {
                userOrders.add(order);
            }
        }
        for (Order order : this.sellList) {
            if (Objects.equals(order.getUsername(), username)) {
                userOrders.add(order);
            }
        }
        sortDateHighToLow(userOrders);
        return userOrders;
    }
}
