package com.example.restservice;
import com.example.restservice.Order;
import com.example.restservice.Trade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Matcher {
    List<Order> buyList;
    List<Order> sellList;
    List<Trade> tradeList;

    public Matcher(List<Order> buyList, List<Order> sellList, List<Trade> tradeList) {
        this.buyList = buyList;
        this.sellList = sellList;
        this.tradeList = tradeList;
    }

    // AddOrder: Takes a new order and returns a list of any trades made (result of searchBuyList/searchSellList)
    public Trade[] AddOrder(Order newOrder) {
        switch(newOrder.action) {
            case "buy":
                this.buyList.add(newOrder);
                // then sort the list
                // then return result of searchBuyList
                break;
            case "sell":
                this.sellList.add(newOrder);
                // then sort the list
                // then return result of searchBuyList
                break;
            default:
                System.out.println("Error: action is " + newOrder.action + ", but must be buy or sell.");
        }
        return new Trade[0];
    }
    // sortList: Takes a list and a setting and sorts it according to setting
    public List<Order> sortPriceHighToLow(ArrayList<Order> list) { // highest price is at Array[0]
        list.sort(Comparator.comparing(Order::getPrice));
        return list;
    }
    public List<Order> sortPriceLowToHigh(ArrayList<Order> list) { // lowest price is at Array[0]
        list.sort(Comparator.comparing(Order::getPrice).reversed());
        return list;
    }
    public List<Order> sortDateHighToLow(ArrayList<Order> list) { // latest date is at Array[0]
        list.sort(Comparator.comparing(Order::getOrderDate));
        return list;
    }
    public List<Order> sortDateLowToHigh(ArrayList<Order> list) { // earliest date is at Array[0]
        list.sort(Comparator.comparing(Order::getOrderDate).reversed());
        return list;
    }
    public List<Trade> sortTradeList(ArrayList<Trade> list) { // latest date is at Array[0] - but using Trade object, not Order object
        list.sort(Comparator.comparing(Trade::getTradeDate));
        return list;
    }
    // searchBuyList: Takes a new order and returns any trades made
    // searchSellList: Takes a new order and returns any trades made


}
