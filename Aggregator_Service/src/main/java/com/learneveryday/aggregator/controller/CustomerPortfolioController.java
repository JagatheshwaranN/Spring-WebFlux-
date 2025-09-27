package com.learneveryday.aggregator.controller;

import com.learneveryday.aggregator.dto.CustomerDetail;
import com.learneveryday.aggregator.dto.StockTradeRequest;
import com.learneveryday.aggregator.dto.StockTradeResponse;
import com.learneveryday.aggregator.dto.TradeRequest;
import com.learneveryday.aggregator.service.CustomerPortfolioService;
import com.learneveryday.aggregator.validator.RequestValidator;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
public class CustomerPortfolioController {

    private final CustomerPortfolioService customerPortfolioService;

    public CustomerPortfolioController(CustomerPortfolioService customerPortfolioService) {
        this.customerPortfolioService = customerPortfolioService;
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerDetail> getCustomerDetail(@PathVariable Integer customerId) {
        return this.customerPortfolioService.getCustomerDetail(customerId);
    }

    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> trade(@PathVariable Integer customerId, @RequestBody Mono<TradeRequest> tradeRequestMono) {
        return tradeRequestMono.transform(RequestValidator.validate())
                .flatMap(request -> this.customerPortfolioService.trade(customerId, request));
    }



}
