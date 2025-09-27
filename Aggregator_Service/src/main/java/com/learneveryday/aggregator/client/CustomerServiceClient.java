package com.learneveryday.aggregator.client;

import com.learneveryday.aggregator.dto.CustomerDetail;
import com.learneveryday.aggregator.dto.StockTradeRequest;
import com.learneveryday.aggregator.dto.StockTradeResponse;
import com.learneveryday.aggregator.exceptions.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.management.monitor.MonitorNotification;
import java.util.Objects;

public class CustomerServiceClient {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceClient.class);
    private final WebClient webClient;


    public CustomerServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<CustomerDetail> getCustomerDetail(Integer customerId) {
        return this.webClient.get()
                .uri("/customers/{customerId}", customerId)
                .retrieve()
                .bodyToMono(CustomerDetail.class)
                .onErrorResume(WebClientResponseException.NotFound.class,
                        exception -> ApplicationException.customerNotFound(customerId));
    }

    public Mono<StockTradeResponse> trade(Integer customerId, StockTradeRequest request) {
        return this.webClient.post()
                .uri("/customers/{customerId}/trade", customerId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(StockTradeResponse.class)
                .onErrorResume(WebClientResponseException.NotFound.class,
                        exception -> ApplicationException.customerNotFound(customerId))
                .onErrorResume(WebClientResponseException.BadRequest.class,
                        this::handleException);
    }

    private <T> Mono<T> handleException(WebClientResponseException.BadRequest exception) {
        var pd = exception.getResponseBodyAs(ProblemDetail.class);
        var message = Objects.nonNull(pd) ? pd.getDetail() : exception.getMessage();
        log.info("Customer service problem detail: {}", pd);
        return ApplicationException.invalidTradeRequest(message);

    }
}
