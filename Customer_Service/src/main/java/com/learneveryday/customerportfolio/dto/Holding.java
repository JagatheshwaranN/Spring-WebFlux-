package com.learneveryday.customerportfolio.dto;

import com.learneveryday.customerportfolio.domain.Ticker;

public record Holding(Ticker ticker, Integer quantity) {
}
