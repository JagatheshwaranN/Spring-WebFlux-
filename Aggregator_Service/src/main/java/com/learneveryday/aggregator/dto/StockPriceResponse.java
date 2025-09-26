package com.learneveryday.aggregator.dto;

import com.learneveryday.aggregator.domain.Ticker;

public record StockPriceResponse(Ticker ticker, Integer price) {
}
