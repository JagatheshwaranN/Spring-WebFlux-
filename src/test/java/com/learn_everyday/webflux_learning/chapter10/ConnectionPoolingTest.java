package com.learn_everyday.webflux_learning.chapter10;

import com.learn_everyday.webflux_learning.chapter10.dto.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

public class ConnectionPoolingTest {

    AbstractWebClient webClient = new AbstractWebClient();

    private final WebClient client = webClient.createWebClient(
            handler -> {
                var poolSize = 501;
                var provider = ConnectionProvider.builder("connection")
                        .lifo()
                        .maxConnectionPools(poolSize)
                        .pendingAcquireMaxCount(poolSize * 5)
                        .build();
                var httpClient = HttpClient.create(provider)
                        .compress(true)
                        .keepAlive(true);
                handler.clientConnector(new ReactorClientHttpConnector(httpClient));
            });


    @Test
    public void concurrentRequest() {
        var max = 501;
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
