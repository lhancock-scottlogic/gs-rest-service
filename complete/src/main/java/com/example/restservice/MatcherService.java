package com.example.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatcherService {
    @Autowired
    private final Matcher matcher = new Matcher(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

    public List<Order> aggOrderBookAPI(@RequestParam String action, @RequestParam int numOfOrders, @RequestParam double pricePoint) {
        return matcher.aggOrderBook(action, numOfOrders, pricePoint);
    }

    public List<Trade> getAllTradesAPI() {
        return matcher.getAllTrades();
    }

    public List<ChartOrder> getAggBuysAPI() {
        return matcher.getAggBuys();
    }

    public List<ChartOrder> getAggSellsAPI() {
        return matcher.getAggSells();
    }

    public List<Order> getSpecificUserOrdersAPI(@RequestParam String username) {
        return matcher.getSpecificUserOrders(username);
    }

    public List<Order> addOrderAPI(@RequestBody Order order) {
        matcher.addOrder(order);
        return matcher.getSpecificUserOrders(order.getUsername());
    }


}
