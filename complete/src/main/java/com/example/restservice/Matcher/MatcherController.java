package com.example.restservice.Matcher;

import com.example.restservice.Account.AccountService;
import com.example.restservice.ChartOrder;
import com.example.restservice.UserOrder;
import com.example.restservice.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/matcher")
public class MatcherController {
    @Autowired
    public MatcherService matcherService;

    @Autowired
    public AccountService accountService;

    @GetMapping("/getAggOrderBook")
    public List<UserOrder> aggOrderBookAPI(@RequestParam (value = "action") String action, @RequestParam (value = "numOfOrders") int numOfOrders, @RequestParam (value = "pricePoint") double pricePoint) {
        return matcherService.aggOrderBookAPI(action, numOfOrders, pricePoint);
    }

    @GetMapping("/getAllTrades")
    public List<Trade> getAllTradesAPI() {
        return matcherService.getAllTradesAPI();
    }

    @GetMapping("/getAggBuys")
    public List<ChartOrder> getAggBuysAPI() {
        return matcherService.getAggBuysAPI();
    }

    @GetMapping("/getAggSells")
    public List<ChartOrder> getAggSellsAPI() {
        return matcherService.getAggSellsAPI();
    }

    @GetMapping("/getSpecificUserOrders")
    public List<UserOrder> getSpecificUserOrdersAPI(@RequestParam(value = "username", defaultValue = "Lucy") String username) {
        return matcherService.getSpecificUserOrdersAPI(username);
    }

    @PostMapping(value = "/addOrder", consumes = "application/json", produces = "application/json")
    public List<UserOrder> addOrderAPI(@Valid @RequestBody UserOrder order) {
        return matcherService.addOrderAPI(order);
    }
}
