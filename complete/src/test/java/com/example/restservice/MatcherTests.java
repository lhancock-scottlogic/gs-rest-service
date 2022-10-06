package com.example.restservice;
import com.example.restservice.Matcher.Matcher;
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

    // ********** UNIT TESTS **********

    /* Methods to write tests for:
    1. sortPriceHighToLow
    2. sortPriceLowToHigh
    3. sortDateHighToLow
    4. sortDateLowToHigh
    5. sortTradeList
    6. removeOrder
    7. createTrade
    8. searchSellList
    9. searchBuyList
    10. addOrder
    11. hasPrice
    12. getAggBuys
    13. getAggSells
    14. aggOrderBook
    15. getAllTrades
    16. getSpecificUserOrders
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
    void SortTradeListTest() {
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
    @DisplayName("searchSellList")
    void SearchSellListTest() {
        // searchSellList should return a Trade arraylist of trades made, or an empty arraylist if no matches are found
        Order testOrder1 = new Order(0, "Account_1", 70, 10, "sell", now);
        Order testOrder2 = new Order(1, "Account_2", 95, 15, "sell", now);
        Order testOrder3 = new Order(2, "Account_3", 95, 15, "sell", now);
        Order testOrder4 = new Order(2, "Account_4", 100, 10, "sell", now);
        Order testOrder5 = new Order(2, "Account_5", 90, 10, "sell", now);

        matcher.addOrder(testOrder1);
        matcher.addOrder(testOrder2);
        matcher.addOrder(testOrder3);
        matcher.addOrder(testOrder4);
        matcher.addOrder(testOrder5);

        ArrayList<Trade> searchResult = matcher.searchSellList(new Order(5,"Account_6", 95, 30, "buy", now));

        ArrayList<Trade> expectedResult = new ArrayList<>();
        expectedResult.add(new Trade("Account_6", "Account_1", 70, 10, now));
        expectedResult.add(new Trade("Account_6", "Account_5", 90, 10, now));
        expectedResult.add(new Trade("Account_6", "Account_3", 95, 10, now));

        assertThat(searchResult).usingRecursiveComparison().isEqualTo(expectedResult);
        assertThat(matcher.tradeList).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("searchBuyList")
    void SearchBuyListTest() {
        // searchBuyList should return a Trade arraylist of trades made, or an empty arraylist if no matches are found
        Order testOrder1 = new Order(0, "Account_1", 70, 10, "buy", now);
        Order testOrder2 = new Order(1, "Account_2", 95, 15, "buy", now);
        Order testOrder3 = new Order(2, "Account_3", 95, 15, "buy", now);
        Order testOrder4 = new Order(2, "Account_4", 100, 10, "buy", now);
        Order testOrder5 = new Order(2, "Account_5", 90, 10, "buy", now);
        matcher.addOrder(testOrder1);
        matcher.addOrder(testOrder2);
        matcher.addOrder(testOrder3);
        matcher.addOrder(testOrder4);
        matcher.addOrder(testOrder5);
        ArrayList<Trade> searchResult = matcher.searchBuyList(new Order(5,"Account_6", 95, 30, "sell", now));
        ArrayList<Trade> expectedResult = new ArrayList<>();
        expectedResult.add(new Trade("Account_4", "Account_6", 100, 10, now));
        expectedResult.add(new Trade("Account_3", "Account_6", 95, 15, now));
        expectedResult.add(new Trade("Account_2", "Account_6", 95, 5, now));
        assertThat(searchResult).usingRecursiveComparison().isEqualTo(expectedResult);
        assertThat(matcher.tradeList).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("addOrder")
    void AddOrder() {
        ArrayList<Trade> testOrder1 = matcher.addOrder(new Order(0, "buyer", 50, 1, "buy", now));
        ArrayList<Trade>  testOrder2 = matcher.addOrder(new Order(1, "seller", 60, 1, "sell", now));
        ArrayList<Trade>  testOrder3 = matcher.addOrder(new Order(2, "buyer", 60, 1, "buy", now));
        ArrayList<Trade>  testOrder4 = matcher.addOrder(new Order(2, "seller", 50, 1, "sell", now));

        ArrayList<Trade> test3Result = new ArrayList<>();
        test3Result.add(new Trade("buyer", "seller", 60, 1, now));

        ArrayList<Trade> test4Result = new ArrayList<>();
        test4Result.add(new Trade("buyer", "seller", 50, 1, now));

        assertThat(testOrder1).usingRecursiveComparison().isEqualTo(new ArrayList<Trade>());
        assertThat(testOrder2).usingRecursiveComparison().isEqualTo(new ArrayList<Trade>());
        assertThat(testOrder3).usingRecursiveComparison().isEqualTo(test3Result);
        assertThat(testOrder4).usingRecursiveComparison().isEqualTo(test4Result);
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
    @DisplayName("getAggBuys")
    void GetAggBuysTest() {
        // getaggbuys should look through buylist and return ALL buy orders in the form of chartOrder objects
        Order testOrder1 = new Order(0, "Account", 50, 10, "buy", now);
        Order testOrder2 = new Order(1, "Account", 50, 10, "buy", now);
        Order testOrder3 = new Order(2, "Account", 25, 10, "buy", now);
        matcher.buyList.add(testOrder1);
        matcher.buyList.add(testOrder2);
        matcher.buyList.add(testOrder3); // add sample orders for processing

        assertThat(matcher.getAggBuys().get(0)).usingRecursiveComparison().isEqualTo(new ChartOrder(25, 10));
        assertThat(matcher.getAggBuys().get(1)).usingRecursiveComparison().isEqualTo(new ChartOrder(50, 30));
    }

    @Test
    @DisplayName("getAggSells")
    void GetAggSellsTest() {
        // getaggsells should look through selllist and return ALL sell orders in the form of chartOrder objects
        Order testOrder1 = new Order(0, "Account", 50, 10, "sell", now);
        Order testOrder2 = new Order(1, "Account", 50, 10, "sell", now);
        Order testOrder3 = new Order(2, "Account", 25, 10, "sell", now);
        matcher.sellList.add(testOrder1);
        matcher.sellList.add(testOrder2);
        matcher.sellList.add(testOrder3); // add sample orders for processing

        assertThat(matcher.getAggSells().get(0)).usingRecursiveComparison().isEqualTo(new ChartOrder(25, 10));
        assertThat(matcher.getAggSells().get(1)).usingRecursiveComparison().isEqualTo(new ChartOrder(50, 30));
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
        assertEquals(matcher.aggOrderBook("sell", 5, 10).size(), 5);
        // all aggregated sells are supplied with a numOfOrders parameter set to -1
        assertEquals(matcher.aggOrderBook("sell", -1, 10).size(), 6);
        assertThat(matcher.aggOrderBook("sell", -1, 10).get(5)).usingRecursiveComparison().isEqualTo(new Order(0, "Account", 90, 11, "sell", now));
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
