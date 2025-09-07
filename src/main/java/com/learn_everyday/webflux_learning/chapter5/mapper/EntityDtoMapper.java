package com.learn_everyday.webflux_learning.chapter5.mapper;

import com.learn_everyday.webflux_learning.chapter5.dto.CustomerDto;
import com.learn_everyday.webflux_learning.chapter5.entity.Customer;

public class EntityDtoMapper {

    public static Customer toEntity(CustomerDto customerDto) {
        var customer = new Customer();
        customer.setId(customerDto.id());
        customer.setName(customerDto.name());
        customer.setEmail(customerDto.email());
        return customer;
    }

    public static CustomerDto toDto(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getName(), customer.getEmail());
    }

}
