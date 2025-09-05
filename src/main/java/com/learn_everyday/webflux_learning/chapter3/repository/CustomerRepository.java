package com.learn_everyday.webflux_learning.chapter3.repository;

import com.learn_everyday.webflux_learning.chapter3.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {

}
