package com.learneveryday.customerportfolio.tests;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
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
        this.client.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(entityExchangeResult -> log.info("{}",
                        new String(Objects.requireNonNull(entityExchangeResult.getResponseBody()))));
    }

}
