package com.learn_everyday.webflux_learning.chapter3.service;

import com.learn_everyday.webflux_learning.chapter3.dto.CustomerDto;
import com.learn_everyday.webflux_learning.chapter3.mapper.EntityDtoMapper;
import com.learn_everyday.webflux_learning.chapter3.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public Flux<CustomerDto> getAllCustomers() {
        return this.customerRepository.findAll().map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> getCustomerById(Integer id) {
        return this.customerRepository.findById(id).map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> saveCustomer(Mono<CustomerDto> customerDtoMono) {
        return customerDtoMono.map(EntityDtoMapper::toEntity)
                .flatMap(this.customerRepository::save)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> updateCustomer(Integer id, Mono<CustomerDto> customerDtoMono) {
        return this.customerRepository.findById(id)
                .flatMap(entity -> customerDtoMono)
                .map(EntityDtoMapper::toEntity)
                .doOnNext(customer -> customer.setId(id))
                .flatMap(this.customerRepository::save)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<Boolean> deleteCustomerById(Integer id) {
        return this.customerRepository.deleteCustomerById(id);
    }

}
