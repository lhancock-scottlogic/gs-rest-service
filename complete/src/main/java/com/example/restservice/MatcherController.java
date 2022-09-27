package com.example.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/matcher")
public class MatcherController {
    @Autowired
    MatcherService matcherService;

    @GetMapping("/getAggOrderBook")
    public List<Order> aggOrderBookAPI(@RequestParam (value = "action") String action, @RequestParam (value = "numOfOrders") int numOfOrders, @RequestParam (value = "pricePoint") double pricePoint) {
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
    public List<Order> getSpecificUserOrdersAPI(@RequestParam(value = "username", defaultValue = "Lucy") String username) {
        return matcherService.getSpecificUserOrdersAPI(username);
    }

    @PostMapping(value = "/addOrder", consumes = "application/json", produces = "application/json")
    public List<Order> addOrderAPI(@RequestBody Order order) {
        return matcherService.addOrderAPI(order);
    }
}
