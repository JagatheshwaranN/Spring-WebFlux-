package com.learneveryday.aggregator.dto;

import com.learneveryday.aggregator.domain.Ticker;
import com.learneveryday.aggregator.domain.TradeAction;

public record StockTradeRequest(Ticker ticker,
                                Integer price,
                                Integer quantity,
                                TradeAction tradeAction) {

    public Integer totalPrice() {
        return price * quantity;
    }
}
