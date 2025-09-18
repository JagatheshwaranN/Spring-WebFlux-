package com.learneveryday.customerportfolio.dto;

import com.learneveryday.customerportfolio.domain.Ticker;
import com.learneveryday.customerportfolio.domain.TradeAction;

public record TradeResponse(Integer customerId,
                            Ticker ticker,
                            Integer price,
                            Integer quantity,
                            TradeAction tradeAction,
                            Integer totalPrice,
                            Integer balance) {
}
