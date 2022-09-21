package com.example.restservice;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// TDD File
public class MatcherTests {
    List<Order> buyArray = new ArrayList<Order>();
    List<Order> sellArray = new ArrayList<Order>();
    List<Trade> tradeArray = new ArrayList<Trade>();
    private LocalDateTime now;
    private Matcher matcher;
    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        matcher = new Matcher(buyArray, sellArray, tradeArray);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("AddOrder returns trades")
    void AddOrderTest() {
        //Trade testTrade = new Trade("Account_1", "Account_2", 50.25f, 10, now);
        //Trade[] testTrades = new Trade[]{testTrade};
        assertEquals((new Trade[]{new Trade("Account_1", "Account_2", 50.25f, 10, now)}), matcher.AddOrder(new Order(1, "Account_2", 50.25, 10, "seller", now)));
    }



}
