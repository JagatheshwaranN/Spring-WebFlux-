package com.learn_everyday.webflux_learning.chapter10;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.function.Consumer;

public class AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(AbstractWebClient.class);

    @Bean
    public WebClient createWebClient() {
        return createWebClient(handler -> {
        });
    }

    public WebClient createWebClient(Consumer<WebClient.Builder> builderConsumer) {

        var builder = WebClient.builder()
                .baseUrl("http://localhost:7070/demo03");
        builderConsumer.accept(builder);
        return builder.build();
    }

    public <T> Consumer<T> print() {
        return item -> log.info("Received: {}", item);
    }

}
