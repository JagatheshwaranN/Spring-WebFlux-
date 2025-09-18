package com.learneveryday.customerportfolio.entity;

import com.learneveryday.customerportfolio.domain.Ticker;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class PortfolioItem {

    @Id
    private Integer id;

    private Customer customerId;

    private Ticker ticker;

    private Integer quantity;

}
