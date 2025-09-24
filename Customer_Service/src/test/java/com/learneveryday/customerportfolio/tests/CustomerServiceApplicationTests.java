package com.learneveryday.customerportfolio.tests;

import com.learneveryday.customerportfolio.domain.Ticker;
import com.learneveryday.customerportfolio.domain.TradeAction;
import com.learneveryday.customerportfolio.dto.TradeRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@SpringBootTest
@AutoConfigureWebTestClient
class CustomerServiceApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceApplicationTests.class);

    @Autowired
    private WebTestClient client;

    @Test
    public void customerDetailTest() {
        getCustomer(1, HttpStatus.OK)
                .jsonPath("$.name").isEqualTo("Sam")
                .jsonPath("$.balance").isEqualTo(10000)
                .jsonPath("$.holdingList").isEmpty();
    }

    @Test
    public void buyAndSellTradeTest() {
        // Buy
        var buyRequest1 = new TradeRequest(Ticker.GOOGLE, 100, 5, TradeAction.BUY);
        trade(2, buyRequest1, HttpStatus.OK)
                .jsonPath("$.balance").isEqualTo(9500);
        var buyRequest2 = new TradeRequest(Ticker.GOOGLE, 100, 5, TradeAction.BUY);
        trade(2, buyRequest2, HttpStatus.OK)
                .jsonPath("$.balance").isEqualTo(9000);
        getCustomer(2, HttpStatus.OK)
                .jsonPath("$.holdingList").isNotEmpty()
                .jsonPath("$.holdingList.length()").isEqualTo(1)
                .jsonPath("$.holdingList[0].ticker").isEqualTo("GOOGLE")
                .jsonPath("$.holdingList[0].quantity").isEqualTo(10);

        // Sell
        var sellRequest1 = new TradeRequest(Ticker.GOOGLE, 110, 5, TradeAction.SELL);
        trade(2, sellRequest1, HttpStatus.OK)
                .jsonPath("$.balance").isEqualTo(9550);
        var sellRequest2 = new TradeRequest(Ticker.GOOGLE, 110, 5, TradeAction.SELL);
        trade(2, sellRequest2, HttpStatus.OK)
                .jsonPath("$.balance").isEqualTo(10100);
        getCustomer(2, HttpStatus.OK)
                .jsonPath("$.holdingList").isNotEmpty()
                .jsonPath("$.holdingList.length()").isEqualTo(1)
                .jsonPath("$.holdingList[0].ticker").isEqualTo("GOOGLE")
                .jsonPath("$.holdingList[0].quantity").isEqualTo(0);

    }

    @Test
    public void customerNotFoundTest() {
        getCustomer(10, HttpStatus.NOT_FOUND)
                .jsonPath("$.detail").isEqualTo("Customer id 10 is not found");

        var buyRequest = new TradeRequest(Ticker.GOOGLE, 100, 5, TradeAction.BUY);
        trade(10, buyRequest, HttpStatus.NOT_FOUND)
                .jsonPath("$.detail").isEqualTo("Customer id 10 is not found");
    }

    @Test
    public void insufficientBalanceTest() {
        var buyRequest = new TradeRequest(Ticker.GOOGLE, 100, 101, TradeAction.BUY);
        trade(3, buyRequest, HttpStatus.BAD_REQUEST)
                .jsonPath("$.detail").isEqualTo("Customer id 3 does not have enough funds to complete the transaction");
    }

    @Test
    public void insufficientSharesTest() {
        var sellRequest = new TradeRequest(Ticker.GOOGLE, 100, 1, TradeAction.SELL);
        trade(3, sellRequest, HttpStatus.BAD_REQUEST)
                .jsonPath("$.detail").isEqualTo("Customer id 3 does not have enough shares to complete the transaction");
    }

    private WebTestClient.BodyContentSpec getCustomer(Integer customerId, HttpStatus expectedStatus) {
        return this.client.get()
                .uri("/customers/{customerId}", customerId)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody()
                .consumeWith(entityExchangeResult -> log.info("{}",
                        new String(Objects.requireNonNull(entityExchangeResult.getResponseBody()))));
    }

    private WebTestClient.BodyContentSpec trade(Integer customerId, TradeRequest tradeRequest, HttpStatus expectedStatus) {
        return this.client.post()
                .uri("/customers/{customerId}/trade", customerId)
                .bodyValue(tradeRequest)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody()
                .consumeWith(entityExchangeResult -> log.info("{}",
                        new String(Objects.requireNonNull(entityExchangeResult.getResponseBody()))));
    }


}
