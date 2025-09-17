package com.learn_everyday.webflux_learning.chapter10;

import com.learn_everyday.webflux_learning.chapter10.dto.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

public class ConnectionPoolingHTTP2Test {

    AbstractWebClient webClient = new AbstractWebClient();

    private final WebClient client = webClient.createWebClient(
            handler -> {
                var poolSize = 1;
                var provider = ConnectionProvider.builder("connection")
                        .lifo()
                        .maxConnectionPools(poolSize)
                        .build();
                var httpClient = HttpClient.create(provider)
                        .protocol(HttpProtocol.H2C)
                        .compress(true)
                        .keepAlive(true);
                handler.clientConnector(new ReactorClientHttpConnector(httpClient));
            });


    @Test
    public void concurrentRequest() {
        var max = 3;
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
