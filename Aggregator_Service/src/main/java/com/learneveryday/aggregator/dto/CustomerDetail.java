package com.learneveryday.aggregator.dto;

import java.util.List;

public record CustomerDetail(Integer id,
                             String name,
                             Integer balance,
                             List<Holding> holdingList) {
}
