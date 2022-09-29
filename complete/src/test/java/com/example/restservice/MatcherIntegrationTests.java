package com.example.restservice;
import com.example.restservice.Matcher.MatcherController;
import com.example.restservice.Matcher.MatcherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MatcherIntegrationTests {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @LocalServerPort
    private int port;

    @Autowired
    MatcherController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    // ********** INTEGRATION TESTS **********
    @BeforeEach
    void setUp() {
        controller.matcherService = new MatcherService(); // reset matcher and its contents

    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    @DisplayName("Users can get aggregated orders with different settings")
    void GetAggregatedOrdersTest() {
        this.controller.addOrderAPI(new Order(0, "Lucy", 51.21, 250, "buy", LocalDateTime.now()));
        this.controller.addOrderAPI(new Order(0, "Lucy", 151.21, 250, "sell", LocalDateTime.now()));
        // Test all orders with action = buy, numOfOrders = first 5, pricePoint = 10
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getAggOrderBook?action=buy&numOfOrders=5&pricePoint=10",
                String.class)).contains("[{\"userId\":0,\"username\":\"Lucy\",\"price\":50.0,\"quantity\":250,\"action\":\"buy\",\"orderDate\":");
        // Test all orders with action = sell, numOfOrders = all (-1), pricePoint = 5
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getAggOrderBook?action=sell&numOfOrders=-1&pricePoint=5",
                String.class)).contains("[{\"userId\":0,\"username\":\"Lucy\",\"price\":150.0,\"quantity\":250,\"action\":\"sell\",\"orderDate\":");
        // Test all order with action = sell, numOfOrders = first 10, pricePoint = 0.5
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getAggOrderBook?action=sell&numOfOrders=10&pricePoint=2.50",
                String.class)).contains("[{\"userId\":0,\"username\":\"Lucy\",\"price\":150.0,\"quantity\":250,\"action\":\"sell\",\"orderDate\":");
        // Test price point = 1.00
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getAggOrderBook?action=sell&numOfOrders=10&pricePoint=1.00",
                String.class)).contains("[{\"userId\":0,\"username\":\"Lucy\",\"price\":151.0,\"quantity\":250,\"action\":\"sell\",\"orderDate\":");
        // Test price point = 0.50
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getAggOrderBook?action=sell&numOfOrders=10&pricePoint=0.5",
                String.class)).contains("[{\"userId\":0,\"username\":\"Lucy\",\"price\":151.0,\"quantity\":250,\"action\":\"sell\",\"orderDate\":");
        // Test price point = 0.10
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getAggOrderBook?action=sell&numOfOrders=10&pricePoint=0.10",
                String.class)).contains("[{\"userId\":0,\"username\":\"Lucy\",\"price\":151.2,\"quantity\":250,\"action\":\"sell\",\"orderDate\":");
        // Test price point = 0.05
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getAggOrderBook?action=sell&numOfOrders=10&pricePoint=0.05",
                String.class)).contains("[{\"userId\":0,\"username\":\"Lucy\",\"price\":151.2,\"quantity\":250,\"action\":\"sell\",\"orderDate\":");
        // Test price point = 0.01
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getAggOrderBook?action=sell&numOfOrders=10&pricePoint=0.01",
                String.class)).contains("[{\"userId\":0,\"username\":\"Lucy\",\"price\":151.21,\"quantity\":250,\"action\":\"sell\",\"orderDate\":");
    }

    @Test
    @DisplayName("Users can view all trades")
    void GetAllTradesTest() {
        this.controller.addOrderAPI(new Order(0, "Lucy", 51.21, 250, "buy", LocalDateTime.now()));
        this.controller.addOrderAPI(new Order(0, "Lucy", 51.21, 250, "sell", LocalDateTime.now()));
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getAllTrades",
                String.class)).contains("[{\"buyerName\":\"Lucy\",\"sellerName\":\"Lucy\",\"price\":51.21,\"quantity\":250,\"tradeDate\":");
    }

    @Test
    @DisplayName("Users can view aggregated buy orders (for chart purposes)")
    void ViewAggBuysTest() {
        for (int i = 0; i < 5; i++) {
            this.controller.addOrderAPI(new Order(0, "Lucy", 51.21, 10, "buy", LocalDateTime.now()));
        }
        this.controller.addOrderAPI(new Order(0, "Lucy", 151.99, 250, "buy", LocalDateTime.now()));
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getAggBuys",
                String.class)).contains("[{\"price\":51.21,\"quantity\":50},{\"price\":151.99,\"quantity\":300}]");
    }

    @Test
    @DisplayName("Users can view aggregated sell orders (for chart purposes)")
    void ViewAggSellsTest() {
        for (int i = 0; i < 5; i++) {
            this.controller.addOrderAPI(new Order(0, "Lucy", 51.21, 10, "sell", LocalDateTime.now()));
        }
        this.controller.addOrderAPI(new Order(0, "Lucy", 151.99, 250, "sell", LocalDateTime.now()));
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getAggSells",
                String.class)).contains("[{\"price\":51.21,\"quantity\":50},{\"price\":151.99,\"quantity\":300}]");
    }

    @Test
    @DisplayName("Users can view their private order book")
    void ViewPrivateOrdersTest() {
        this.controller.addOrderAPI(new Order(0, "Lucy", 1, 1, "sell", LocalDateTime.now()));
        this.controller.addOrderAPI(new Order(0, "Lucy", 2.5, 250, "sell", LocalDateTime.now()));
        this.controller.addOrderAPI(new Order(0, "Lucy", 33, 333, "sell", LocalDateTime.now()));
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getSpecificUserOrders?username=Lucy",
                String.class)).contains("{\"userId\":0,\"username\":\"Lucy\",\"price\":1.0,\"quantity\":1,\"action\":\"sell\",\"orderDate\":");
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getSpecificUserOrders?username=Lucy",
                String.class)).contains("{\"userId\":0,\"username\":\"Lucy\",\"price\":2.5,\"quantity\":250,\"action\":\"sell\",\"orderDate\":");
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getSpecificUserOrders?username=Lucy",
                String.class)).contains("{\"userId\":0,\"username\":\"Lucy\",\"price\":33.0,\"quantity\":333,\"action\":\"sell\",\"orderDate\":");
    }

    @Test
    @DisplayName("Users can make a new order")
    void MakeNewOrderTest() {
        this.controller.addOrderAPI(new Order(0, "Lucy", 1, 1, "sell", LocalDateTime.now()));
        // it appears in private order book
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getSpecificUserOrders?username=Lucy",
                String.class)).contains("{\"userId\":0,\"username\":\"Lucy\",\"price\":1.0,\"quantity\":1,\"action\":\"sell\",\"orderDate\":");
        // it appears in aggregated order book
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getAggOrderBook?action=sell&numOfOrders=-1&pricePoint=1.0",
                String.class)).contains("[{\"userId\":0,\"username\":\"Lucy\",\"price\":1.0,\"quantity\":1,\"action\":\"sell\",\"orderDate\":");
        // it appears in aggregated sells
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/matcher/getAggSells",
                String.class)).contains("[{\"price\":1.0,\"quantity\":1}]");
    }

    //********** NEW ORDER VALIDATION **********
    @Test
    @DisplayName("New orders are validated correctly")
    void ValidateOrdersTest() {
        List<Order> badOrderList = new ArrayList<>();
        // No null fields cannot be tested since IDE detects incorrect typing before runtime
        // userId > 0
        badOrderList.add(new Order(-1, "Lucy", 1, 1, "buy", LocalDateTime.now()));
        // username is between 2 and 30 characters
        badOrderList.add(new Order(0, "L", 1, 1, "buy", LocalDateTime.now()));
        badOrderList.add(new Order(0, "Lucyyyyyyyyyyyyyyyyyyyyyyyyyyyy", 1, 1, "buy", LocalDateTime.now()));
        // price is >= £0.01
        badOrderList.add(new Order(0, "Lucy", 0, 1, "buy", LocalDateTime.now()));
        badOrderList.add(new Order(0, "Lucy", -1, 1, "buy", LocalDateTime.now()));
        badOrderList.add(new Order(0, "Lucy", 0.000000001, 1, "buy", LocalDateTime.now()));
        // quantity is >= 1
        badOrderList.add(new Order(0, "Lucy", 1, 0, "buy", LocalDateTime.now()));
        badOrderList.add(new Order(0, "Lucy", 1, -1, "buy", LocalDateTime.now()));
        // orderDate is in the past
        badOrderList.add(new Order(0, "Lucy", 1, 1, "buy", LocalDateTime.parse("2100-03-29T13:34:00.000")));
        // Check each order one by one and assert each one causes a violation
        for (Order badOrder : badOrderList) {
            Set<ConstraintViolation<Order>> violations = validator.validate(badOrder);
            System.out.println(violations);
            assertFalse(violations.isEmpty());
        }
    }
}
