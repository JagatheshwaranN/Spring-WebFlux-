package com.learneveryday.customerportfolio.mapper;

import com.learneveryday.customerportfolio.domain.Ticker;
import com.learneveryday.customerportfolio.dto.CustomerDetail;
import com.learneveryday.customerportfolio.dto.Holding;
import com.learneveryday.customerportfolio.dto.TradeRequest;
import com.learneveryday.customerportfolio.dto.TradeResponse;
import com.learneveryday.customerportfolio.entity.Customer;
import com.learneveryday.customerportfolio.entity.PortfolioItem;

import java.util.List;

public class EntityDtoMapper {

    public static CustomerDetail toCustomerDetail(Customer customer, List<PortfolioItem> items) {
        var holdings = items.stream()
                .map(i -> new Holding(i.getTicker(), i.getQuantity()))
                .toList();
        return new CustomerDetail(
                customer.getId(),
                customer.getName(),
                customer.getBalance(),
                holdings
        );
    }

    public static PortfolioItem toPortfolioItem(Integer customerId, Ticker ticker) {
        var portfolioItem = new PortfolioItem();
        portfolioItem.setCustomerId(customerId);
        portfolioItem.setTicker(ticker);
        portfolioItem.setQuantity(0);
        return portfolioItem;
    }

    public static TradeResponse toTradeResponse(TradeRequest tradeRequest, Integer customerId, Integer balance) {
        return new TradeResponse(
                customerId,
                tradeRequest.ticker(),
                tradeRequest.price(),
                tradeRequest.quantity(),
                tradeRequest.tradeAction(),
                tradeRequest.totalPrice(),
                balance
        );
    }
}
