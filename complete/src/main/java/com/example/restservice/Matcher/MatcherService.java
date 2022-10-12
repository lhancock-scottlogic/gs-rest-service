package com.example.restservice.Matcher;
import com.example.restservice.ChartOrder;
import com.example.restservice.UserOrder;
import com.example.restservice.Trade;
import com.example.restservice.model.AccountModel;
import com.example.restservice.repository.AccountRepository;
import com.example.restservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    AccountRepository accountRepository; // possibly a bad idea to use accountRepository here

    @Autowired
    OrderRepository orderRepository;

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    public List<UserOrder> aggOrderBookAPI(@RequestParam String action, @RequestParam int numOfOrders, @RequestParam double pricePoint) {
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

    public List<UserOrder> getSpecificUserOrdersAPI(@RequestParam String username) {
        String actual_username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return matcher.getSpecificUserOrders(actual_username);
    }

    public List<UserOrder> addOrderAPI(@RequestBody UserOrder order) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // infer username from currently authenticated user
        order.setUsername(username);
        order.setaccountId(getIdFromUserLogin(username)); // infer account ID from DB query using username (ID not given in SecurityContextHolder)
        //matcher.addOrder(order);
        UserOrder dbOrder = orderRepository.save(order);
        matcher.addOrder(dbOrder);
        //orderRepository.save(order);
        return matcher.getSpecificUserOrders(order.getUsername());
    }

    public int getIdFromUserLogin(String username) { // possibly a bad idea to use accountRepository here
        List<AccountModel> accountList = (List<AccountModel>) accountRepository.findAll();
        for (AccountModel account : accountList) {
            if (account.getUsername().equals(username)) {
                return account.getId();
            }
        }
        return -1;
    }
}
