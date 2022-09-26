package com.example.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatcherService {
    @Autowired
    private final Matcher matcher = new Matcher(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

    public List<Trade> getAllTradesAPI() {
        Trade newTrade = new Trade("buyer", "seller", 50, 50, LocalDateTime.now());
        matcher.tradeList.add(newTrade);
        return matcher.getAllTrades();
    }

    public List<Order> newOrderAPI(@RequestBody Order order) {
        matcher.addOrder(order);
        return matcher.getSpecificUserOrders(order.getUsername());
    }
}
