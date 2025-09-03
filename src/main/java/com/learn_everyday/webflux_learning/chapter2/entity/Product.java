package com.learn_everyday.webflux_learning.chapter2.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Product {

    @Id
    private int id;
    private String description;
    private int price;

}
