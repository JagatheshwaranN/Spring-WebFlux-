package com.learn_everyday.webflux_learning.chapter2.repository;

import com.learn_everyday.webflux_learning.chapter2.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {

    public Flux<Customer> findByName(String name);

    public Flux<Customer> findByEmailEndsWith(String email);
}
