package com.learneveryday.aggregator.client;

import com.learneveryday.aggregator.domain.Ticker;
import com.learneveryday.aggregator.dto.PriceUpdate;
import com.learneveryday.aggregator.dto.StockPriceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;


public class StockServiceClient {

    private static final Logger log = LoggerFactory.getLogger(StockServiceClient.class);
    private final WebClient client;
    private Flux<PriceUpdate> priceUpdateFlux;

    public StockServiceClient(WebClient client) {
        this.client = client;
    }

    public Mono<StockPriceResponse> getStockPrice(Ticker ticker) {
        return this.client.get()
                .uri("/stock/{ticker}", ticker)
                .retrieve()
                .bodyToMono(StockPriceResponse.class);
    }

    public Flux<PriceUpdate> priceUpdateStream() {
        if(Objects.isNull(this.priceUpdateFlux)) {
            this.priceUpdateFlux = this.getPriceUpdate();
        }
        return this.priceUpdateFlux;
    }

    private Flux<PriceUpdate> getPriceUpdate() {
        return this.client.get()
                .uri("/stock/price-stream")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(PriceUpdate.class)
                .retryWhen(retry())
                .cache(1);
    }

    private Retry retry() {
        return Retry.fixedDelay(100, Duration.ofSeconds(1))
                .doBeforeRetry(retrySignal -> log.error("Stock service price stream call failed. Retrying: {}", retrySignal.failure().getMessage()));
    }
}
