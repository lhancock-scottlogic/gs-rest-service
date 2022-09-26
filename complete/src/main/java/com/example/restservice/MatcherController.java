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

    @GetMapping("/getAllTrades")
    public List<Trade> getAllTradesAPI() {
        return matcherService.getAllTradesAPI();
    }

    @PostMapping(value = "/newOrder", consumes = "application/json", produces = "application/json")
    public List<Order> newOrderAPI(@RequestBody Order order) {
        return matcherService.newOrderAPI(order);
    }
}
