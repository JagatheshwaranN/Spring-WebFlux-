package com.learn_everyday.webflux_learning.chapter9.repository;

import com.learn_everyday.webflux_learning.chapter9.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

}
