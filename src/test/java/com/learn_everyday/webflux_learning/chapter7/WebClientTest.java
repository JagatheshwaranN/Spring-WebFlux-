package com.learn_everyday.webflux_learning.chapter7;

import com.learn_everyday.webflux_learning.chapter7.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

public class WebClientTest {

    AbstractWebClient abstractWebClient = new AbstractWebClient();

    private final WebClient webClient = abstractWebClient.createWebClient();

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

}
