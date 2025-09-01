package com.learn_everyday.webflux_learning.chapter2;

import com.learn_everyday.webflux_learning.chapter2.repository.CustomerRepository;
import io.r2dbc.h2.util.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

public class CustomerTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerTest.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void findAllCustomers() {
        this.customerRepository
                .findAll()
                .doOnNext(customer -> log.info("{}", customer))
                .as(StepVerifier::create)
                .expectNextCount(10)
                .expectComplete()
                .verify();
    }

    @Test
    public void findCustomerById() {
        this.customerRepository
                .findById(2)
                .doOnNext(customer -> log.info("{}", customer))
                .as(StepVerifier::create)
                .assertNext(customer -> Assertions.assertEquals("mike", customer.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void findCustomerByName() {
        this.customerRepository
                .findByName("jake")
                .doOnNext(customer -> log.info("{}", customer))
                .as(StepVerifier::create)
                .assertNext(customer -> Assertions.assertEquals("jake@gmail.com", customer.getEmail()))
                .expectComplete()
                .verify();
    }

    @Test
    public void findCustomerByEmailEndsWith() {
        this.customerRepository
                .findByEmailEndsWith("ke@gmail.com")
                .doOnNext(customer -> log.info("{}", customer))
                .as(StepVerifier::create)
                .assertNext(customer -> Assertions.assertEquals("mike@gmail.com", customer.getEmail()))
                .assertNext(customer -> Assertions.assertEquals("jake@gmail.com", customer.getEmail()))
                .expectComplete()
                .verify();
    }

}
