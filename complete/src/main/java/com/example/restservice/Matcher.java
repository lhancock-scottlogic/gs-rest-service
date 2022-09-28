package com.example.restservice;
import org.springframework.stereotype.Service;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
//import java.util.List;
@Service
public class Matcher {
    List<Order> buyList;
    List<Order> sellList;
    List<Trade> tradeList;

    // ********** Constructor **********
    public Matcher(ArrayList<Order> buyList, ArrayList<Order> sellList, ArrayList<Trade> tradeList) {
        this.buyList = buyList;
        this.sellList = sellList;
        this.tradeList = tradeList;
    }

//    public static void main(String[] args) {
//        Matcher matcher = new Matcher(new ArrayList<Order>(), new ArrayList<Order>(), new ArrayList<Trade>());
//        matcher.addOrder(new Order(0, "Lucy", 10, 10, "buy", LocalDateTime.now()));
//        System.out.println(matcher.aggOrderBook("buy", 10, 10));
//    }

    // ********** LIST SORT METHODS **********
    public List<Order> sortPriceHighToLow(List<Order> list) { // highest price is at Array[0]
        list.sort(Comparator.comparing(Order::getPrice).reversed());
        return list;
    }
    public List<Order> sortPriceLowToHigh(List<Order> list) { // lowest price is at Array[0]
        list.sort(Comparator.comparing(Order::getPrice));
        return list;
    }
    public ArrayList<Order> sortDateHighToLow(ArrayList<Order> list) { // latest date is at Array[0]
        list.sort(Comparator.comparing(Order::getOrderDate).reversed());
        return list;
    }
    public List<Order> sortDateLowToHigh(List<Order> list) { // earliest date is at Array[0]
        list.sort(Comparator.comparing(Order::getOrderDate));
        return list;
    }
    public List<Trade> sortTradeList(List<Trade> list) { // latest date is at Array[0] - but using Trade object, not Order object
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
                this.buyList = sortPriceLowToHigh(this.buyList);
                return searchSellList(newOrder); // then return result of searchBuyList
            case "sell":
                this.sellList.add(newOrder);
                this.sellList = sortDateLowToHigh(this.sellList); // sort by earliest added, then by lowest price
                this.sellList = sortPriceHighToLow(this.sellList);
                return searchBuyList(newOrder); // then return result of searchBuyList
            default:
                System.out.println("Error: action is " + newOrder.action + ", but must be buy or sell.");
                return new ArrayList<>(); // return empty ArrayList if there's an error to prevent frontend collapsing
        }
    }

    // searchSellList: Takes a new order and returns an ArrayList of any trades made, or an empty list if none
    public ArrayList<Trade> searchSellList(Order newOrder) {
        ArrayList<Trade> currentTradeList = new ArrayList<>();
        if (this.sellList.size() > 0) {
            for (int i = this.sellList.size() - 1; i >= 0; i--) {
                if (this.sellList.get(i).getPrice() <= newOrder.getPrice()) {
                    if (this.sellList.get(i).getQuantity() > newOrder.getQuantity()) {
                        this.sellList.get(i).setQuantity(this.sellList.get(i).getQuantity() - newOrder.getQuantity());
                        Trade newTrade = createTrade(newOrder.getUsername(), this.sellList.get(i).getUsername(), this.sellList.get(i).getPrice(), newOrder.getQuantity(), newOrder.getOrderDate());
                        removeOrder(newOrder);
                        currentTradeList.add(newTrade);
                        i = -1;
                    }
                    else if (newOrder.getQuantity() > this.sellList.get(i).getQuantity()) {
                        newOrder.setQuantity(newOrder.getQuantity() - this.sellList.get(i).getQuantity()); // newOrder quantity is reduced but not depleted
                        Trade newTrade = createTrade(newOrder.getUsername(), this.sellList.get(i).getUsername(), this.sellList.get(i).getPrice(), this.sellList.get(i).getQuantity(), newOrder.getOrderDate()); // a trade of the smaller quantity (i)
                        this.removeOrder(this.sellList.get(i)); // i's quantity is now depleted so we delete it
                        currentTradeList.add(newTrade);
                    }
                    else { // If buyer and seller quantities are equal, then remove both orders as their quantities will be depleted
                        Trade newTrade = createTrade(newOrder.getUsername(), this.sellList.get(i).getUsername(), this.sellList.get(i).getPrice(), newOrder.getQuantity(), newOrder.getOrderDate()); // Create a new trade first
                        removeOrder(this.sellList.get(i)); // fetch indexes of buyer and seller orders in their respective lists and destroy them
                        removeOrder(newOrder);
                        currentTradeList.add(newTrade);
                        i = -1;
                    }
                }
            }
        }
        return currentTradeList;
    }

    // searchBuyList: Takes a new order and returns an ArrayList of any trades made, or an empty list if none
    public ArrayList<Trade> searchBuyList(Order newOrder) {
        ArrayList<Trade> currentTradeList = new ArrayList<>();
        if (this.buyList.size() > 0) {
            for (int i = this.buyList.size() - 1; i >= 0; i--) { // Look in buyList
                if ((this.buyList.get(i).getPrice() >= newOrder.getPrice())) { // as the seller, you want a buyer to pay equal to or more than your asking price
                    if (this.buyList.get(i).getQuantity() > newOrder.getQuantity()) {
                        this.buyList.get(i).setQuantity(this.buyList.get(i).getQuantity() - newOrder.getQuantity()); // quantity reduced due to partial trade
                        Trade newTrade = createTrade(this.buyList.get(i).getUsername(), newOrder.getUsername(), this.buyList.get(i).getPrice(), newOrder.getQuantity(), newOrder.getOrderDate());
                        removeOrder(newOrder); // qty depleted, so it gets deleted
                        currentTradeList.add(newTrade);
                        i = -1;
                    }
                    else if (newOrder.getQuantity() > this.buyList.get(i).getQuantity()) {
                        newOrder.setQuantity(newOrder.quantity - this.buyList.get(i).getQuantity()); // quantity reduced due to partial trade
                        Trade newTrade = createTrade(this.buyList.get(i).getUsername(), newOrder.getUsername(), this.buyList.get(i).getPrice(), this.buyList.get(i).getQuantity(), newOrder.getOrderDate());
                        this.removeOrder(this.buyList.get(i)); // quantity depleted, so it gets deleted
                        currentTradeList.add(newTrade);
                    }
                    else {
                        Trade newTrade = createTrade(this.buyList.get(i).getUsername(), newOrder.getUsername(), this.buyList.get(i).getPrice(), this.buyList.get(i).getQuantity(), newOrder.getOrderDate());
                        this.removeOrder(newOrder); // destroy both orders
                        this.removeOrder(this.buyList.get(i));
                        currentTradeList.add(newTrade);
                        i = -1;
                    }
                }
            }
        }
        return currentTradeList;
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

    public ArrayList<ChartOrder> getAggBuys() {
        ArrayList<Order> aggBuyList = new ArrayList<>();
        int index;
        int accumulator = 0;
        ArrayList<ChartOrder> chartBuyList = new ArrayList<>();
        for (Order order : this.buyList) {
            if (hasPrice(aggBuyList, order.price) == -1) {
                aggBuyList.add(order);
            }
            else {
                index = hasPrice(aggBuyList, order.price);
                aggBuyList.get(index).setQuantity(aggBuyList.get(index).getQuantity() + order.quantity);
            }
        }
        sortPriceLowToHigh(aggBuyList);
        for (Order order : aggBuyList) {
            chartBuyList.add(new ChartOrder(order.getPrice(), order.getQuantity() + accumulator));
            accumulator = accumulator + order.getQuantity();
        }
        return chartBuyList;
    }

    public ArrayList<ChartOrder> getAggSells() {
        ArrayList<Order> aggSellList = new ArrayList<>();
        int index;
        int accumulator = 0;
        ArrayList<ChartOrder> chartSellList = new ArrayList<>();
        for (Order order : this.sellList) {
            index = hasPrice(aggSellList, order.price);
            if (index == -1) {
                aggSellList.add(order);
            }
            else {
                index = hasPrice(aggSellList, order.price);
                aggSellList.get(index).setQuantity(aggSellList.get(index).getQuantity() + order.quantity);
            }
        }
        sortPriceLowToHigh(aggSellList);
        for (Order order : aggSellList) {
            chartSellList.add(new ChartOrder(order.getPrice(), order.getQuantity() + accumulator));
            accumulator = accumulator + order.getQuantity();
        }
        return chartSellList;
    }

    public List<Order> aggOrderBook(String action, int numOfOrders, double pricePoint) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.UP);
        List<Order> list;
        ArrayList<Order> aggList = new ArrayList<Order>();
        if (Objects.equals(action, "buy")) {
            list = this.buyList;
        }
        else {
            list = this.sellList;
        }
//        else {
//            throw new Exception("Error in aggOrderBook call, action is " + action + ", but must be buy or sell");
//        }
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
            return aggList.subList(0, numOfOrders);
        }

        return aggList;
    }

    public List<Trade> getAllTrades() {
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
