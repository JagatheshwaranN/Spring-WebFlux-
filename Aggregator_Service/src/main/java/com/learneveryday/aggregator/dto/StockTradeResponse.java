package com.learneveryday.aggregator.dto;

import com.learneveryday.aggregator.domain.Ticker;
import com.learneveryday.aggregator.domain.TradeAction;

public record StockTradeResponse(Integer customerId,
                                 Ticker ticker,
                                 Integer price,
                                 Integer quantity,
                                 TradeAction tradeAction,
                                 Integer totalPrice,
                                 Integer balance) {
}
