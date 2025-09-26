package com.learneveryday.aggregator.dto;


import com.learneveryday.aggregator.domain.Ticker;

public record Holding(Ticker ticker, Integer quantity) {
}
