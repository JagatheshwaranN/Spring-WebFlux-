package com.learneveryday.customerportfolio.service;

import com.learneveryday.customerportfolio.dto.TradeRequest;
import com.learneveryday.customerportfolio.dto.TradeResponse;
import com.learneveryday.customerportfolio.entity.Customer;
import com.learneveryday.customerportfolio.entity.PortfolioItem;
import com.learneveryday.customerportfolio.exceptions.ApplicationException;
import com.learneveryday.customerportfolio.mapper.EntityDtoMapper;
import com.learneveryday.customerportfolio.repository.CustomerRepository;
import com.learneveryday.customerportfolio.repository.PortfolioItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class TradeService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PortfolioItemRepository portfolioItemRepository;

    @Transactional
    public Mono<TradeResponse> trade(Integer customerId, TradeRequest tradeRequest) {
        return switch (tradeRequest.tradeAction()) {
            case BUY -> this.buyStock(customerId, tradeRequest);
            case SELL -> this.sellStock(customerId, tradeRequest);
        };
    }

    private Mono<TradeResponse> buyStock(Integer customerId, TradeRequest tradeRequest) {
        var customerMono = this.customerRepository.findById(customerId)
                .switchIfEmpty(ApplicationException.customerNotFound(customerId))
                .filter(customer -> customer.getBalance() >= tradeRequest.totalPrice())
                .switchIfEmpty(ApplicationException.insufficientBalance(customerId));
        var portfolioItemMono = this.portfolioItemRepository.findByCustomerIdAndTicker(customerId, tradeRequest.ticker())
                .defaultIfEmpty(EntityDtoMapper.toPortfolioItem(customerId, tradeRequest.ticker()));
        return customerMono.zipWhen(customer -> portfolioItemMono)
                .flatMap(objects -> this.executeBuy(objects.getT1(), objects.getT2(), tradeRequest));
    }

    private Mono<TradeResponse> executeBuy(Customer customer, PortfolioItem portfolioItem, TradeRequest tradeRequest) {
        customer.setBalance(customer.getBalance() - tradeRequest.totalPrice());
        portfolioItem.setQuantity(portfolioItem.getQuantity() + tradeRequest.quantity());
        return this.saveAndBuildResponse(customer, portfolioItem, tradeRequest);
    }

    private Mono<TradeResponse> sellStock(Integer customerId, TradeRequest tradeRequest) {
        var customerMono = this.customerRepository.findById(customerId)
                .switchIfEmpty(ApplicationException.customerNotFound(customerId));
        var portfolioItemMono = this.portfolioItemRepository.findByCustomerIdAndTicker(customerId, tradeRequest.ticker())
                .filter(portfolioItem -> portfolioItem.getQuantity() >= tradeRequest.quantity())
                .switchIfEmpty(ApplicationException.insufficientShares(customerId));
        return customerMono.zipWhen(customer -> portfolioItemMono)
                .flatMap(objects -> this.executeSell(objects.getT1(), objects.getT2(), tradeRequest));
    }

    private Mono<TradeResponse> executeSell(Customer customer, PortfolioItem portfolioItem, TradeRequest tradeRequest) {
        customer.setBalance(customer.getBalance() + tradeRequest.totalPrice());
        portfolioItem.setQuantity(portfolioItem.getQuantity() - tradeRequest.quantity());
        return this.saveAndBuildResponse(customer, portfolioItem, tradeRequest);
    }

    private Mono<TradeResponse> saveAndBuildResponse(Customer customer, PortfolioItem portfolioItem, TradeRequest tradeRequest) {
        var response = EntityDtoMapper.toTradeResponse(tradeRequest, customer.getId(), customer.getBalance());
        return Mono.zip(this.customerRepository.save(customer), this.portfolioItemRepository.save(portfolioItem))
                .thenReturn(response);
    }

}
