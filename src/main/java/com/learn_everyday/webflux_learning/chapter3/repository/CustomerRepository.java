package com.learn_everyday.webflux_learning.chapter3.repository;

import com.learn_everyday.webflux_learning.chapter3.entity.Customer;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {

    @Modifying
    @Query("Delete From Customer Where Id=:id")
    Mono<Boolean> deleteCustomerById(Integer id);

}
