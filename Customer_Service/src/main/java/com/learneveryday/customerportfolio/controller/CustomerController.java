package com.learneveryday.customerportfolio.controller;

import com.learneveryday.customerportfolio.dto.CustomerDetail;
import com.learneveryday.customerportfolio.dto.TradeRequest;
import com.learneveryday.customerportfolio.dto.TradeResponse;
import com.learneveryday.customerportfolio.service.CustomerService;
import com.learneveryday.customerportfolio.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TradeService tradeService;

    @GetMapping("/{customerId}")
    public Mono<CustomerDetail> getCustomerDetail(@PathVariable Integer customerId) {
        return this.customerService.getCustomerDetail(customerId);
    }

    @PostMapping("/{customerId}/trade")
    public Mono<TradeResponse> trade(@PathVariable Integer customerId, @RequestBody Mono<TradeRequest> tradeRequestMono) {
        return tradeRequestMono.flatMap(tradeRequest -> this.tradeService.trade(customerId, tradeRequest));
    }

}
