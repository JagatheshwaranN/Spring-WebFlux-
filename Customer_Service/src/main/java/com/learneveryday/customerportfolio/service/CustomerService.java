package com.learneveryday.customerportfolio.service;

import com.learneveryday.customerportfolio.dto.CustomerDetail;
import com.learneveryday.customerportfolio.exceptions.ApplicationException;
import com.learneveryday.customerportfolio.repository.CustomerRepository;
import com.learneveryday.customerportfolio.repository.PortfolioItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    PortfolioItemRepository portfolioItemRepository;

//    public Mono<CustomerDetail> getCustomerDetail(Integer customerId) {
//        return this.customerRepository.findById(customerId)
//                .switchIfEmpty(ApplicationException.customerNotFound(customerId));
//    }
}
