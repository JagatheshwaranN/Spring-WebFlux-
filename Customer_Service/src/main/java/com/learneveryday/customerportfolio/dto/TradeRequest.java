package com.learneveryday.customerportfolio.dto;

import com.learneveryday.customerportfolio.domain.Ticker;
import com.learneveryday.customerportfolio.domain.TradeAction;

public record TradeRequest(Ticker ticker,
                           Integer price,
                           Integer quantity,
                           TradeAction tradeAction) {

    public Integer totalPrice() {
        return price * quantity;
    }
}
