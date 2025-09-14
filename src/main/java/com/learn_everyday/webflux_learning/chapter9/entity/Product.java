package com.learn_everyday.webflux_learning.chapter9.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data

public class Product {

    @Id
    private Integer id;

    private String description;

    private Integer price;
}
