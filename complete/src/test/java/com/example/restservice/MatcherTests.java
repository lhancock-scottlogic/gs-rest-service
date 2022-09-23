package com.example.restservice;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

    /* Methods to write tests for:
    1. sortPriceHighToLow /
    2. sortPriceLowToHigh /
    3. sortDateHighToLow /
    4. sortDateLowToHigh /
    5. sortTradeList /
    6. removeOrder /
    7. createTrade /
    8. searchSellList
    9. searchBuyList
    10. addOrder
    11. hasPrice /
    12. getAggBuys
    13. getAggSells
    14. aggOrderBook
    15. getAllTrades /
    16. getSpecificUserOrders /
    */

    @Test
    @DisplayName("sortPriceHighToLow")
    void SortPriceHighToLowTest() {
        ArrayList<Order> testList = new ArrayList<Order>();
        Order testOrder1 = new Order(0, "Account_1", 50.63, 100, "buy", now);
        Order testOrder2 = new Order(1, "Account_2", 182.99, 999, "buy", now);
        Order testOrder3 = new Order(2, "Account_3", 50.64, 17, "buy", now);
        testList.add(testOrder1);
        testList.add(testOrder2);
        testList.add(testOrder3);
        matcher.sortPriceHighToLow(testList);
        assertThat(testList.get(0)).usingRecursiveComparison().isEqualTo(testOrder2);
        assertThat(testList.get(1)).usingRecursiveComparison().isEqualTo(testOrder3);
        assertThat(testList.get(2)).usingRecursiveComparison().isEqualTo(testOrder1);
    }

    @Test
    @DisplayName("sortPriceLowToHigh")
    void SortPriceLowToHighTest() {
        ArrayList<Order> testList = new ArrayList<Order>();
        Order testOrder1 = new Order(0, "Account_1", 50.63, 100, "buy", now);
        Order testOrder2 = new Order(1, "Account_2", 182.99, 999, "buy", now);
        Order testOrder3 = new Order(2, "Account_3", 50.64, 17, "buy", now);
        testList.add(testOrder1);
        testList.add(testOrder2);
        testList.add(testOrder3);
        matcher.sortPriceLowToHigh(testList);
        assertThat(testList.get(0)).usingRecursiveComparison().isEqualTo(testOrder1);
        assertThat(testList.get(1)).usingRecursiveComparison().isEqualTo(testOrder3);
        assertThat(testList.get(2)).usingRecursiveComparison().isEqualTo(testOrder2);
    }

    @Test
    @DisplayName("sortDateHighToLow")
    void SortDateHighToLowTest() {
        ArrayList<Order> testList = new ArrayList<>();
        Order testOrder1 = new Order(0, "Account_1", 50.63, 100, "buy", LocalDateTime.of(2019, 3, 28, 14, 33));
        Order testOrder2 = new Order(1, "Account_2", 182.99, 999, "buy", LocalDateTime.of(2019, 3, 28, 14, 34));
        Order testOrder3 = new Order(2, "Account_3", 50.64, 17, "buy", now);
        testList.add(testOrder1);
        testList.add(testOrder2);
        testList.add(testOrder3);
        matcher.sortDateHighToLow(testList);
        assertThat(testList.get(0)).usingRecursiveComparison().isEqualTo(testOrder3);
        assertThat(testList.get(1)).usingRecursiveComparison().isEqualTo(testOrder2);
        assertThat(testList.get(2)).usingRecursiveComparison().isEqualTo(testOrder1);
    }

    @Test
    @DisplayName("sortDateLowToHigh")
    void SortDateLowToHighTest() {
        ArrayList<Order> testList = new ArrayList<>();
        Order testOrder1 = new Order(0, "Account_1", 50.63, 100, "buy", LocalDateTime.of(2019, 3, 28, 14, 33));
        Order testOrder2 = new Order(1, "Account_2", 182.99, 999, "buy", LocalDateTime.of(2019, 3, 28, 14, 34));
        Order testOrder3 = new Order(2, "Account_3", 50.64, 17, "buy", now);
        testList.add(testOrder3);
        testList.add(testOrder2);
        testList.add(testOrder1);
        matcher.sortDateLowToHigh(testList);
        assertThat(testList.get(0)).usingRecursiveComparison().isEqualTo(testOrder1);
        assertThat(testList.get(1)).usingRecursiveComparison().isEqualTo(testOrder2);
        assertThat(testList.get(2)).usingRecursiveComparison().isEqualTo(testOrder3);
    }

    @Test
    @DisplayName("sortTradeList")
    void SortTradeListTest() { // TODO: refactor to use Trades
        ArrayList<Trade> testList = new ArrayList<>();
        Trade testTrade1 = new Trade("Buyer", "Seller", 99.99, 150, LocalDateTime.of(2019, 3, 28, 14, 33));
        Trade testTrade2 = new Trade("Buyer", "Seller", 189.99, 22, LocalDateTime.of(2019, 3, 28, 14, 34));
        Trade testTrade3 = new Trade("Buyer", "Seller", 50.65, 16, now);
        testList.add(testTrade1);
        testList.add(testTrade2);
        testList.add(testTrade3);
        matcher.sortTradeList(testList);
        assertThat(testList.get(0)).usingRecursiveComparison().isEqualTo(testTrade3);
        assertThat(testList.get(1)).usingRecursiveComparison().isEqualTo(testTrade2);
        assertThat(testList.get(2)).usingRecursiveComparison().isEqualTo(testTrade1);
    }

    @Test
    @DisplayName("removeOrder removes orders")
    void RemoveOrderTest() {
        // ********** Object gets removed from buyList **********
        ArrayList<Order> testBuyOrders = new ArrayList<Order>();
        testBuyOrders.add(new Order(0, "Account_1", 50.63, 100, "buy", now));
        testBuyOrders.add(new Order(2, "Account_3", 182.99, 999, "buy", now));
        matcher.buyList.add(new Order(0, "Account_1", 50.63, 100, "buy", now));
        matcher.buyList.add(new Order(1, "Account_2", 77.09, 75, "buy", now));
        matcher.buyList.add(new Order(2, "Account_3", 182.99, 999, "buy", now));
        assertEquals(1, matcher.removeOrder(matcher.buyList.get(1)));
        assertThat(matcher.buyList).usingRecursiveComparison().isEqualTo(testBuyOrders);

        // ********** Object gets removed from sellList **********
        ArrayList<Order> testSellOrders = new ArrayList<Order>();
        testSellOrders.add(new Order(0, "Account_1", 50.63, 100, "sell", now));
        testSellOrders.add(new Order(2, "Account_3", 182.99, 999, "sell", now));
        matcher.sellList.add(new Order(0, "Account_1", 50.63, 100, "sell", now));
        matcher.sellList.add(new Order(1, "Account_2", 77.09, 75, "sell", now));
        matcher.sellList.add(new Order(2, "Account_3", 182.99, 999, "sell", now));
        assertEquals(1, matcher.removeOrder(matcher.sellList.get(1)));
        assertThat(matcher.sellList).usingRecursiveComparison().isEqualTo(testSellOrders);

        // ********** Object doesn't get removed if it doesn't exist in either list **********
        testBuyOrders.add(new Order(1, "Account_2", 77.09, 75, "buy", now));
        testSellOrders.add(new Order(1, "Account_2", 77.09, 75, "sell", now));
        assertEquals(0, matcher.removeOrder(testBuyOrders.get(2)));
        assertEquals(0, matcher.removeOrder(testSellOrders.get(2)));
    }

    @Test
    @DisplayName("createTrade creates trades")
    void CreateTradeTest() {
        Trade expectedResult = new Trade("Buyer", "Seller", 80, 10, now);
        Trade actualResult =  matcher.createTrade("Buyer", "Seller", 80, 10, now);
        // Test that matcher returns the expected Trade object when called
        assertThat(expectedResult).usingRecursiveComparison().isEqualTo(actualResult);
        ArrayList<Trade> testTrades = new ArrayList<Trade>();
        testTrades.add(new Trade("Buyer", "Seller", 80, 10, now));
        // Test that matcher adds the Trade object to its tradeList
        assertThat(matcher.tradeList).usingRecursiveComparison().isEqualTo(testTrades);
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

    @Test
    @DisplayName("aggOrderBook")
    void AggOrderBookTest() throws Exception {
        Order testOrder1 = new Order(0, "Account_1", 50.5, 100, "buy", now);
        Order testOrder2 = new Order(1, "Account_2", 50.42, 75, "buy", now);
        Order testOrder3 = new Order(2, "Account_3", 50.99, 999, "sell", now);
        matcher.buyList.add(testOrder1);
        matcher.buyList.add(testOrder2);
        matcher.sellList.add(new Order(0, "Account", 90.01, 11, "sell", now));
        for(int i = 0; i < 5; i++) {
            matcher.sellList.add(testOrder3); // adding 5 of these (+ above element) so that we can test displaying the first 5 orders
        }
        // aggregated buys are supplied with any price point, and orders are limited to 10, and agg buys are sorted price high to low
        assertThat(matcher.aggOrderBook("buy", 10, 0.1).get(0)).usingRecursiveComparison().isEqualTo(new Order(0, "Account_1", 50.5, 100, "buy", now));
        assertThat(matcher.aggOrderBook("buy", 10, 0.1).get(1)).usingRecursiveComparison().isEqualTo(new Order(1, "Account_2", 50.4, 75, "buy", now));
        assertEquals(matcher.aggOrderBook("buy", 10, 0.1).size(), 2);
        // aggregated sells are supplied with a different price point, and orders are limited to first 5, and agg sells are sorted price low to high
        for (int i = 0; i < 4; i++) {
            assertThat(matcher.aggOrderBook("sell", 5, 10).get(i)).usingRecursiveComparison().isEqualTo(new Order(2, "Account_3", 50, 999, "sell", now));
        }
        assertThat(matcher.aggOrderBook("sell", 5, 10).get(4)).usingRecursiveComparison().isEqualTo(new Order(0, "Account", 90, 11, "sell", now));
        assertEquals(matcher.aggOrderBook("sell", 5, 10).size(), 5);
        // all aggregated sells are supplied with a numOfOrders parameter set to -1
        assertEquals(matcher.aggOrderBook("sell", -1, 10).size(), 6);
    }

    @Test
    @DisplayName("getAllTrades")
    void GetAllTradesTest() {
        Trade testTrade1 = new Trade("Buyer", "Seller", 12, 1, now);
        ArrayList<Trade> testTrades = new ArrayList<>();
        testTrades.add(testTrade1);
        testTrades.add(testTrade1);
        testTrades.add(testTrade1);
        matcher.tradeList.add(testTrade1);
        matcher.tradeList.add(testTrade1);
        matcher.tradeList.add(testTrade1);
        assertThat(matcher.getAllTrades()).usingRecursiveComparison().isEqualTo(testTrades);
    }

    @Test
    @DisplayName("getSpecificUserOrders")
    void GetSpecificUserOrdersTest() {
        Order testOrder1 = new Order(0, "Account_1", 50.63, 100, "buy", now);
        Order testOrder2 = new Order(1, "Account_2", 77.09, 75, "buy", now);
        Order testOrder3 = new Order(2, "Account_3", 182.99, 999, "sell", now);
        ArrayList<Order> testOrders = new ArrayList<Order>();
        matcher.buyList.add(testOrder1);
        matcher.buyList.add(testOrder2);
        matcher.sellList.add(testOrder3);
        ArrayList<Order> testList = new ArrayList<>();
        testList.add(testOrder2);
        assertThat(matcher.getSpecificUserOrders("Account_2")).usingRecursiveComparison().isEqualTo(testList);
    }



}
