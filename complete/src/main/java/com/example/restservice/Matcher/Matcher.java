package com.example.restservice.Matcher;
import com.example.restservice.ChartOrder;
import com.example.restservice.UserOrder;
import com.example.restservice.Trade;
import com.example.restservice.repository.OrderRepository;
import com.example.restservice.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
//import java.util.List;

@Service
public class Matcher {
    public List<UserOrder> buyList;
    public List<UserOrder> sellList;
    public List<Trade> tradeList;

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    OrderRepository orderRepository;

    // ********** Constructor **********
    public Matcher(ArrayList<UserOrder> buyList, ArrayList<UserOrder> sellList, ArrayList<Trade> tradeList) {
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
    public List<UserOrder> sortPriceHighToLow(List<UserOrder> list) { // highest price is at Array[0]
        list.sort(Comparator.comparing(UserOrder::getPrice).reversed());
        return list;
    }
    public List<UserOrder> sortPriceLowToHigh(List<UserOrder> list) { // lowest price is at Array[0]
        list.sort(Comparator.comparing(UserOrder::getPrice));
        return list;
    }
    public ArrayList<UserOrder> sortDateHighToLow(ArrayList<UserOrder> list) { // latest date is at Array[0]
        list.sort(Comparator.comparing(UserOrder::getOrderDate).reversed());
        return list;
    }
    public List<UserOrder> sortDateLowToHigh(List<UserOrder> list) { // earliest date is at Array[0]
        list.sort(Comparator.comparing(UserOrder::getOrderDate));
        return list;
    }
    public List<Trade> sortTradeList(List<Trade> list) { // latest date is at Array[0] - but using Trade object, not Order object
        list.sort(Comparator.comparing(Trade::getTradeDate).reversed());
        return list;
    }

    public int removeOrder(UserOrder orderToRemove) {
        if (Objects.equals(orderToRemove.getOrderAction(), "buy")) {
            if (buyList.contains(orderToRemove)) {
                buyList.remove(orderToRemove);
                //System.out.println(this.getMatchingOrderFromDatabase(orderToRemove));
                //orderRepository.deleteById(this.getMatchingOrderFromDatabase(orderToRemove).getId()); // todo: try deleting by username
                orderRepository.deleteById(orderToRemove.getId());
                return 1;
            }
        }
        if (Objects.equals(orderToRemove.getOrderAction(), "sell")) {
            if (sellList.contains(orderToRemove)) {
                sellList.remove(orderToRemove);
                //System.out.println(this.getMatchingOrderFromDatabase(orderToRemove));
                //orderRepository.deleteById(this.getMatchingOrderFromDatabase(orderToRemove).getId());
                orderRepository.deleteById(orderToRemove.getId());
                return 1;
            }
        }
        return 0;
    }

    public UserOrder getMatchingOrderFromDatabase(UserOrder order) {
        Iterable<UserOrder> orderList = orderRepository.findAll();
        for(UserOrder repo_order : orderList) {
            if (order.equals(repo_order)) {
                System.out.println("getMatchingOrderFromDatabase returned " + repo_order);
                return repo_order;
            }
        }
        System.out.println("getMatchingOrderFromDatabase returned null");
        return null;
    }

    public Trade createTrade(String buyer, String seller, double price, int quantity, LocalDateTime tradeDate) {
        try {
            Trade newTrade = new Trade(buyer, seller, price, quantity, tradeDate);
            this.tradeList.add(newTrade);
            tradeRepository.save(newTrade);
            return newTrade;
        }
        catch(Exception e) {
            System.out.println("Error in createTrade call: " + e + ", returning null.");
            return null;
        }
    }

    // AddOrder: Takes a new order and returns a list of any trades made (result of searchBuyList/searchSellList)
    public ArrayList<Trade> addOrder(UserOrder newOrder) {
        switch(newOrder.getOrderAction()) {
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
                System.out.println("Error: action is " + newOrder.getOrderAction() + ", but must be buy or sell.");
                return new ArrayList<>(); // return empty ArrayList if there's an error to prevent frontend collapsing
        }
    }

    // searchSellList: Takes a new order and returns an ArrayList of any trades made, or an empty list if none
    public ArrayList<Trade> searchSellList(UserOrder newOrder) {
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
    public ArrayList<Trade> searchBuyList(UserOrder newOrder) {
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
                        newOrder.setQuantity(newOrder.getQuantity() - this.buyList.get(i).getQuantity()); // quantity reduced due to partial trade
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
    public int hasPrice(ArrayList<UserOrder> list, double amount) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPrice() == amount) {
                return i;
            }
        }
        return -1; // if nothing was returned by now, it means the price is not in the list
    }

    public ArrayList<ChartOrder> getAggBuys() {
        ArrayList<UserOrder> aggBuyList = new ArrayList<>();
        int index;
        int accumulator = 0;
        ArrayList<ChartOrder> chartBuyList = new ArrayList<>();
        for (UserOrder order : this.buyList) {
            if (hasPrice(aggBuyList, order.getPrice()) == -1) {
                aggBuyList.add(new UserOrder(order)); // object is cloned as a DEEP copy using 2nd Order constructor
            }
            else {
                index = hasPrice(aggBuyList, order.getPrice());
                aggBuyList.get(index).setQuantity(aggBuyList.get(index).getQuantity() + order.getQuantity());
            }
        }
        sortPriceLowToHigh(aggBuyList);
        for (UserOrder order : aggBuyList) {
            chartBuyList.add(new ChartOrder(order.getPrice(), order.getQuantity() + accumulator));
            accumulator = accumulator + order.getQuantity();

        }
        return chartBuyList;
    }

    public ArrayList<ChartOrder> getAggSells() {
        ArrayList<UserOrder> aggSellList = new ArrayList<>();
        int index;
        int accumulator = 0;
        ArrayList<ChartOrder> chartSellList = new ArrayList<>();
        for (UserOrder order : this.sellList) {
            index = hasPrice(aggSellList, order.getPrice());
            if (index == -1) {
                aggSellList.add(new UserOrder(order));
            }
            else {
                index = hasPrice(aggSellList, order.getPrice());
                aggSellList.get(index).setQuantity(aggSellList.get(index).getQuantity() + order.getQuantity());
            }
        }
        sortPriceLowToHigh(aggSellList);
        for (UserOrder order : aggSellList) {
            chartSellList.add(new ChartOrder(order.getPrice(), order.getQuantity() + accumulator));
            accumulator = accumulator + order.getQuantity();
        }
        return chartSellList;
    }

    public List<UserOrder> aggOrderBook(String action, int numOfOrders, double pricePoint) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.UP);
        List<UserOrder> list;
        ArrayList<UserOrder> aggList = new ArrayList<UserOrder>();
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
        for (UserOrder order : list) { // run through order objects in the list (enhanced for)
            if (Math.round(order.getPrice() / pricePoint) == 0) {
                price = pricePoint;
            } else {
                price = Math.round(order.getPrice() / pricePoint) * pricePoint;
            }
            aggList.add(new UserOrder(order.getaccountId(), order.getUsername(), (Math.round(price * 100.0) / 100.0), order.getQuantity(), order.getOrderAction(), order.getOrderDate()));
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

    public ArrayList<UserOrder> getSpecificUserOrders(String username) {
        ArrayList<UserOrder> userOrders = new ArrayList<>();
        for (UserOrder order : this.buyList) {
            if (Objects.equals(order.getUsername(), username)) {
                userOrders.add(order);
            }
        }
        for (UserOrder order : this.sellList) {
            if (Objects.equals(order.getUsername(), username)) {
                userOrders.add(order);
            }
        }
        sortDateHighToLow(userOrders);
        return userOrders;
    }
}
