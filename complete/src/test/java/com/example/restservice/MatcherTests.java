package com.example.restservice;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// TDD File
public class MatcherTests {
    ArrayList<Order> buyArray = new ArrayList<Order>();
    ArrayList<Order> sellArray = new ArrayList<Order>();
    ArrayList<Trade> tradeArray = new ArrayList<Trade>();
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

//    @Test
//    @DisplayName("AddOrder returns trades")
//    void AddOrderTest() {
//        //Trade testTrade = new Trade("Account_1", "Account_2", 50.25f, 10, now);
//        //Trade[] testTrades = new Trade[]{testTrade};
//        assertEquals((new Trade[]{new Trade("Account_1", "Account_2", 50.25f, 10, now)}), matcher.AddOrder(new Order(1, "Account_2", 50.25, 10, "seller", now)));
//    }

    @Test
    @DisplayName("createTrade creates trades")
    void CreateTradeTest() {
        Trade expectedResult = new Trade("Buyer", "Seller", 80, 10, now);
        // matcher returns the expected Trade object when called
        assertEquals(expectedResult, matcher.createTrade("Buyer", "Seller", 80, 10, now));
        // matcher adds the Trade object to its tradeList
        ArrayList<Trade> testTrades = new ArrayList<Trade>();
        testTrades.add(new Trade("Buyer", "Seller", 80, 10, now));
        assertEquals(matcher.tradeList, testTrades);
    }

    @Test
    @DisplayName("createTrade throws error when passed invalid data")
    void CreateTradeTestError() {

    }

    @Test
    @DisplayName("hasPrice gives correct index of price in ArrayList")
    void HasPriceTest() {
        Order testOrder1 = new Order(0, "Account_1", 50.63, 100, "buy", now);
        Order testOrder2 = new Order(1, "Account_2", 77.09, 75, "buy", now);
        Order testOrder3 = new Order(2, "Account_3", 182.99, 999, "sell", now);
        ArrayList<Order> testOrders = new ArrayList<Order>();
        testOrders.add(testOrder1);
        testOrders.add(testOrder2);
        testOrders.add(testOrder3);
        assertEquals(0, matcher.hasPrice(testOrders, 50.63));
        assertEquals(1, matcher.hasPrice(testOrders, 77.09));
        assertEquals(2, matcher.hasPrice(testOrders, 182.99));
    }

    @Test
    @DisplayName("hasPrice returns -1 when no price index is present in ArrayList")
    void HasPriceTestError() {
        Order testOrder1 = new Order(0, "Account_1", 50.63, 100, "buy", now);
        Order testOrder2 = new Order(1, "Account_2", 77.09, 75, "buy", now);
        Order testOrder3 = new Order(2, "Account_3", 182.99, 999, "sell", now);
        ArrayList<Order> testOrders = new ArrayList<Order>();
        testOrders.add(testOrder1);
        testOrders.add(testOrder2);
        testOrders.add(testOrder3);
        assertEquals(-1, matcher.hasPrice(testOrders, 50.7));
    }



}
