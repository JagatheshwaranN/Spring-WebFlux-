package com.learn_everyday.webflux_learning.chapter2;

import com.learn_everyday.webflux_learning.chapter2.entity.Customer;
import com.learn_everyday.webflux_learning.chapter2.repository.CustomerRepository;
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

    @Test
    public void insertAndDeleteCustomer() {
        // Insert
        var newCustomer = new Customer();
        newCustomer.setName("jane");
        newCustomer.setEmail("jane@gmail.com");
        this.customerRepository.save(newCustomer)
                .doOnNext(customer -> log.info("{}", customer))
                .as(StepVerifier::create)
                .assertNext(customer -> Assertions.assertNotNull(customer.getId()))
                .expectComplete()
                .verify();

        this.customerRepository.count()
                .as(StepVerifier::create)
                .expectNext(11L)
                .expectComplete()
                .verify();

        // Delete
        this.customerRepository.deleteById(11)
                .then(this.customerRepository.count())
                .as(StepVerifier::create)
                .expectNext(10L)
                .expectComplete()
                .verify();
    }

    @Test
    public void updateCustomer() {
        this.customerRepository.findByName("ethan")
                .doOnNext(customer -> customer.setName("novel"))
                .flatMap(customer -> this.customerRepository.save(customer))
                .doOnNext(customer -> log.info("{}", customer))
                .as(StepVerifier::create)
                .assertNext(customer -> Assertions.assertNotNull(customer.getId()))
                .expectComplete()
                .verify();
    }

}
