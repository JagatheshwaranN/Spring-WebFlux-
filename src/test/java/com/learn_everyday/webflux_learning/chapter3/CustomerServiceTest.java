package com.learn_everyday.webflux_learning.chapter3;

import com.learn_everyday.webflux_learning.chapter3.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "chapter3")
public class CustomerServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void allCustomers() {
        this.webTestClient.get()
                .uri("/customers")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CustomerDto.class)
                .value(list -> log.info("{}", list))
                .hasSize(10);
    }

    @Test
    public void allPaginatedCustomers() {
        this.webTestClient.get()
                .uri("/customers/paginated?pageOffset=2&size=2")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(entityExchangeResult -> {
                    log.info("{}", new String(Objects.requireNonNull(entityExchangeResult.getResponseBody())));
                })
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].id").isEqualTo(3)
                .jsonPath("$[1].id").isEqualTo(4);
    }

    @Test
    public void customerById() {
        this.webTestClient.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(entityExchangeResult -> {
                    log.info("{}", new String(Objects.requireNonNull(entityExchangeResult.getResponseBody())));
                })
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("sam")
                .jsonPath("$.email").isEqualTo("sam@gmail.com");
    }

    @Test
    public void createAndDeleteCustomer() {
        var newCustomer = new CustomerDto(null, "miha", "miha@gmail.com");

        // create
        this.webTestClient.post()
                .uri("/customers")
                .bodyValue(newCustomer)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(entityExchangeResult -> {
                    log.info("{}", new String(Objects.requireNonNull(entityExchangeResult.getResponseBody())));
                })
                .jsonPath("$.id").isEqualTo(11)
                .jsonPath("$.name").isEqualTo("miha")
                .jsonPath("$.email").isEqualTo("miha@gmail.com");

        // delete
        this.webTestClient.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().isEmpty();
    }

    @Test
    public void updateCustomer() {
        var updateCustomer = new CustomerDto(null, "novel", "novel@gmail.com");

        // create
        this.webTestClient.put()
                .uri("/customers/10")
                .bodyValue(updateCustomer)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(entityExchangeResult -> {
                    log.info("{}", new String(Objects.requireNonNull(entityExchangeResult.getResponseBody())));
                })
                .jsonPath("$.id").isEqualTo(10)
                .jsonPath("$.name").isEqualTo("novel")
                .jsonPath("$.email").isEqualTo("novel@gmail.com");
    }

    @Test
    public void customerNotFound() {

        // get
        this.webTestClient.get()
                .uri("/customers/11")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().isEmpty();

        // delete
        this.webTestClient.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().isEmpty();

        // put
        this.webTestClient.put()
                .uri("/customers/11")
                .bodyValue(new CustomerDto(null, "jane", "jane@gmail.com"))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().isEmpty();
    }

}
