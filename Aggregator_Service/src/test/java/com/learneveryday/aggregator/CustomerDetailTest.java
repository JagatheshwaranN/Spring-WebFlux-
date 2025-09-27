package com.learneveryday.aggregator;

import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

public class CustomerDetailTest extends AggregatorApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(CustomerDetailTest.class);

    @Test
    public void customerDetail() {
        mockCustomerDetail("customer-service/customer-detail-200.json", 200);
        getCustomerDetail(HttpStatus.OK)
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Sam")
                .jsonPath("$.balance").isEqualTo(10000)
                .jsonPath("$.holdingList").isNotEmpty();
    }

    @Test
    public void customerNotFound() {
        mockCustomerDetail("customer-service/customer-detail-404.json", 404);
        getCustomerDetail(HttpStatus.NOT_FOUND)
                .jsonPath("$.detail").isEqualTo("Customer id 1 is not found");

    }

    private void mockCustomerDetail(String path, int responseCode) {
        var responseBody = this.resourcesToString(path);

        mockServerClient
                .when(HttpRequest.request("/customers/1"))
                .respond(HttpResponse.response(responseBody)
                        .withStatusCode(responseCode)
                        .withContentType(MediaType.APPLICATION_JSON));

    }

    private WebTestClient.BodyContentSpec getCustomerDetail(HttpStatus expectedStatus) {
        return this.client.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody()
                .consumeWith(entityExchangeResult ->
                        log.info("{}", new String(Objects.requireNonNull(entityExchangeResult.getResponseBody()))));
    }
}
