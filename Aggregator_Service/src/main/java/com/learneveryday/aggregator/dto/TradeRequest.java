package com.learneveryday.aggregator.dto;

import com.learneveryday.aggregator.domain.Ticker;
import com.learneveryday.aggregator.domain.TradeAction;

public record TradeRequest(Ticker ticker, TradeAction tradeAction, Integer quantity) {
}
