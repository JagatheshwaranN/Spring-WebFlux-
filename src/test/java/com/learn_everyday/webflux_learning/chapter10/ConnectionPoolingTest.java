package com.learn_everyday.webflux_learning.chapter10;

import com.learn_everyday.webflux_learning.chapter10.dto.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ConnectionPoolingTest {

    AbstractWebClient webClient = new AbstractWebClient();

    private final WebClient client = webClient.createWebClient();


    @Test
    public void concurrentRequest() {
        var max = 1;
        Flux.range(1, max)
                .flatMap(this::getProduct)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(list -> Assertions.assertEquals(max, list.size()))
                .expectComplete()
                .verify();
    }

    private Mono<Product> getProduct(int id) {
        return this.client.get()
                .uri("/product/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }

}
