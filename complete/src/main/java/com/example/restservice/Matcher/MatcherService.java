package com.example.restservice.Matcher;
import com.example.restservice.ChartOrder;
import com.example.restservice.Order;
import com.example.restservice.Trade;
import com.example.restservice.model.AccountModel;
import com.example.restservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatcherService {
    @Autowired
    private final Matcher matcher = new Matcher(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

    @Autowired
    AccountRepository accountRepository;

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

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

    public List<Order> addOrderAPI(@RequestBody Order order) { // todo: infer userId from their record too
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        order.setUsername(username);
        matcher.addOrder(order);
        return matcher.getSpecificUserOrders(order.getUsername());
    }


}
