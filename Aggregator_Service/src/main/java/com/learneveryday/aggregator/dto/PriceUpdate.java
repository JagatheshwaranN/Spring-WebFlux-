package com.learneveryday.aggregator.dto;

import com.learneveryday.aggregator.domain.Ticker;

import java.time.LocalDateTime;

public record PriceUpdate(Ticker ticker, Integer price, LocalDateTime localDateTime) {
}
