package com.learn_everyday.webflux_learning.chapter2.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
public class CustomerOrder {

    @Id
    private UUID id;
    private int customerId;
    private int productId;
    private int amount;
    private Instant orderDate;

}
