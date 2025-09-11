package com.learn_everyday.webflux_learning.chapter6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "chapter6")
public class CalculatorTest {

    private static final Logger log = LoggerFactory.getLogger(CalculatorTest.class);

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void calculatorTest() {
        validate(20, 10, "+", 200, "30");
        validate(20, 10, "-", 200, "10");
        validate(20, 10, "*", 200, "200");
        validate(20, 10, "/", 200, "2");
        validate(20, 0, "+", 400, "b cannot be 0.");
        validate(20, 10, "@", 400, "Operations header should be of +|-|*|/");
        validate(20, 10, null, 400, "Operations header should be of +|-|*|/");
    }

    private void validate(int a, int b, String operation, int statusCode, String expectedResult) {
        this.webTestClient.get()
                .uri("/calculator/{a}/{b}", a, b)
                .headers(httpHeaders -> {
                    if (Objects.nonNull(operation)) {
                        httpHeaders.add("operations", operation);
                    }
                })
                .exchange()
                .expectStatus().isEqualTo(statusCode)
                .expectBody(String.class)
                .value(data -> {
                    Assertions.assertNotNull(data);
                    Assertions.assertEquals(expectedResult, data);
                });
    }

}
