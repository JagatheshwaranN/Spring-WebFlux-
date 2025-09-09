package com.learn_everyday.webflux_learning.chapter5;

import com.learn_everyday.webflux_learning.chapter5.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "chapter5")
public class CustomerServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void noTokenTest() {
        this.webTestClient.get()
                .uri("/customers")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void invalidTokenTest() {
        this.webTestClient.get()
                .uri("/customers")
                .header("auth-token", "secret")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void standardCategoryAllCustomersTest() {
        this.webTestClient.get()
                .uri("/customers")
                .header("auth-token", "secret123")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);
    }

    @Test
    public void primeCategoryAllCustomersTest() {
        this.webTestClient.get()
                .uri("/customers")
                .header("auth-token", "secret456")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);
    }

    @Test
    public void standardCategoryCreateAndDeleteCustomer() {
        var newCustomer = new CustomerDto(null, "miha", "miha@gmail.com");

        // create
        this.webTestClient.post()
                .uri("/customers")
                .header("auth-token", "secret123")
                .bodyValue(newCustomer)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FORBIDDEN);

        // delete
        this.webTestClient.delete()
                .uri("/customers/11")
                .header("auth-token", "secret123")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void primeCategoryUpdateCustomer() {
        var updateCustomer = new CustomerDto(null, "novel", "novel@gmail.com");

        this.webTestClient.put()
                .uri("/customers/10")
                .header("auth-token", "secret456")
                .bodyValue(updateCustomer)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);
    }

}
