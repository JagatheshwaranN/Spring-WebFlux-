package com.learneveryday.customerportfolio.dto;

import java.util.List;

public record CustomerDetail(Integer id,
                             String name,
                             Integer balance,
                             List<Holding> holdingList) {
}
