package com.example.restservice;
import com.example.restservice.Order;
import com.example.restservice.Trade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public Trade createTrade(String buyer, String seller, double price, int quantity, LocalDateTime tradeDate) {
//        try {
//
//        }
//        catch(Exception e) {
//
//        }
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
    // ********** LIST SORT METHODS **********
    public ArrayList<Order> sortPriceHighToLow(ArrayList<Order> list) { // highest price is at Array[0]
        list.sort(Comparator.comparing(Order::getPrice));
        return list;
    }
    public ArrayList<Order> sortPriceLowToHigh(ArrayList<Order> list) { // lowest price is at Array[0]
        list.sort(Comparator.comparing(Order::getPrice).reversed());
        return list;
    }
    public ArrayList<Order> sortDateHighToLow(ArrayList<Order> list) { // latest date is at Array[0]
        list.sort(Comparator.comparing(Order::getOrderDate));
        return list;
    }
    public ArrayList<Order> sortDateLowToHigh(ArrayList<Order> list) { // earliest date is at Array[0]
        list.sort(Comparator.comparing(Order::getOrderDate).reversed());
        return list;
    }
    public ArrayList<Trade> sortTradeList(ArrayList<Trade> list) { // latest date is at Array[0] - but using Trade object, not Order object
        list.sort(Comparator.comparing(Trade::getTradeDate));
        return list;
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
}
