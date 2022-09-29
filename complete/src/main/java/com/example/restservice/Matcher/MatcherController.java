package com.example.restservice.Matcher;

import com.example.restservice.ChartOrder;
import com.example.restservice.Order;
import com.example.restservice.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/matcher")
public class MatcherController {
    @Autowired
    public
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
    public List<Order> addOrderAPI(@Valid @RequestBody Order order) {
        return matcherService.addOrderAPI(order);
    }
}
