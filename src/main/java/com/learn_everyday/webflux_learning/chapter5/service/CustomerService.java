package com.learn_everyday.webflux_learning.chapter5.service;

import com.learn_everyday.webflux_learning.chapter5.dto.CustomerDto;
import com.learn_everyday.webflux_learning.chapter5.mapper.EntityDtoMapper;
import com.learn_everyday.webflux_learning.chapter5.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Flux<CustomerDto> getAllCustomers(Integer pageOffset, Integer size) {
        return this.customerRepository.findBy(PageRequest.of(pageOffset -1, size))
                .map(EntityDtoMapper::toDto);
    }

    public Mono<Page<CustomerDto>> getAllCustomersInPage(PageRequest pageRequest) {
//        return this.customerRepository.findAllBy(pageRequest)
//                .collectList()
//                .zipWith(this.customerRepository.count())
//                .map(tuple -> new PageImpl<>(tuple.getT1(), pageRequest, tuple.getT2()));
        return null;
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
