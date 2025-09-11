package com.learn_everyday.webflux_learning.chapter7;

import com.learn_everyday.webflux_learning.chapter7.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Map;

public class WebClientTest {

    AbstractWebClient abstractWebClient = new AbstractWebClient();

    private final WebClient webClient = abstractWebClient.createWebClient();
    private final WebClient webClientWithHeader = abstractWebClient.createWebClient(header -> header.defaultHeader("caller-id", "order-service"));

    @Test
    public void getTest() throws InterruptedException {
        this.webClient.get()
                .uri("/lec01/product/1")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(abstractWebClient.print())
                .subscribe();

        Thread.sleep(Duration.ofSeconds(2));
    }

    @Test
    public void concurrentGetTest() throws InterruptedException {
        for (int i = 1; i <= 50; i++) {
            this.webClient.get()
                    .uri("/lec01/product/{id}", i)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(abstractWebClient.print())
                    .subscribe();
        }
        Thread.sleep(Duration.ofSeconds(5));
    }

    @Test
    public void getStreamingResponseTest() throws InterruptedException {
        this.webClient.get()
                .uri("/lec02/product/stream")
                .retrieve()
                .bodyToFlux(Product.class)
                .take(Duration.ofSeconds(3))
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void postBodyValueTest() {
        var mono = new Product(null, "iwatch", 500);
        this.webClient.post()
                .uri("/lec03/product")
                .bodyValue(mono)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }


    @Test
    public void postBodyTest() {
        var mono = Mono.fromSupplier(() -> new Product(null, "iwatch", 500))
                .delayElement(Duration.ofSeconds(1));
        this.webClient.post()
                .uri("/lec03/product")
                .body(mono, Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void defaultHeaderTest() {
        this.webClientWithHeader.get()
                .uri("/lec04/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void overrideDefaultHeaderTest() {
        this.webClientWithHeader.get()
                .uri("/lec04/product/{id}", 1)
                .header("caller-id", "new-service")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void multipleHeadersTest() {
        var map = Map.of("caller-id", "order-service",
                "new-key", "new-value");
        this.webClientWithHeader.get()
                .uri("/lec04/product/{id}", 1)
                .headers(httpHeaders -> httpHeaders.setAll(map))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

}
