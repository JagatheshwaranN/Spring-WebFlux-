package com.learneveryday.aggregator.config;

import com.learneveryday.aggregator.client.CustomerServiceClient;
import com.learneveryday.aggregator.client.StockServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ServiceClientsConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ServiceClientsConfiguration.class);

    @Bean
    public CustomerServiceClient customerServiceClient(@Value("${customer.service.url}") String baseURL) {
        return new CustomerServiceClient(createWebClient(baseURL));
    }

    @Bean
    public StockServiceClient stockServiceClient(@Value("${stock.service.url}") String baseURL) {
        return new StockServiceClient(createWebClient(baseURL));
    }

    private WebClient createWebClient(String baseURL) {
        log.info("Base URL :{}", baseURL);
        return WebClient.builder()
                .baseUrl(baseURL)
                .build();
    }

}
