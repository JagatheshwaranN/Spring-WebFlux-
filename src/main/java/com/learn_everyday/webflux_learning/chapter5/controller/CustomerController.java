package com.learn_everyday.webflux_learning.chapter5.controller;

import com.learn_everyday.webflux_learning.chapter5.dto.CustomerDto;
import com.learn_everyday.webflux_learning.chapter5.exception.ApplicationException;
import com.learn_everyday.webflux_learning.chapter5.filter.Category;
import com.learn_everyday.webflux_learning.chapter5.service.CustomerService;
import com.learn_everyday.webflux_learning.chapter5.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping
    public Flux<CustomerDto> allCustomers(@RequestAttribute("category") Category category) {
        System.out.println(category);
        return this.customerService.getAllCustomers();
    }

//    @GetMapping("paginated")
//    public Flux<CustomerDto> allCustomers(@RequestParam(defaultValue = "1") Integer pageOffset, @RequestParam(defaultValue = "3") Integer size) {
//        return this.customerService.getAllCustomers(pageOffset, size);
//    }

    @GetMapping("paginated")
    public Mono<List<CustomerDto>> allCustomers(@RequestParam(defaultValue = "1") Integer pageOffset, @RequestParam(defaultValue = "3") Integer size) {
        return this.customerService.getAllCustomers(pageOffset, size).collectList();
    }

    @GetMapping("paginated/all")
    public Mono<Page<CustomerDto>> allCustomersInPage(@RequestParam(defaultValue = "1") Integer pageOffset, @RequestParam(defaultValue = "3") Integer size) {
        return this.customerService.getAllCustomersInPage(PageRequest.of(pageOffset, size));
    }

    @GetMapping("{id}")
    public Mono<CustomerDto> getCustomer(@PathVariable Integer id) {
        return this.customerService.getCustomerById(id)
                .switchIfEmpty(ApplicationException.customerNotFound(id));
    }

    @PostMapping
    public Mono<CustomerDto> saveCustomer(@RequestBody Mono<CustomerDto> customerDtoMono) {
        return customerDtoMono.transform(RequestValidator.validate())
                .as(this.customerService::saveCustomer);
    }

    @PutMapping("{id}")
    public Mono<CustomerDto> updateCustomer(@PathVariable Integer id, @RequestBody Mono<CustomerDto> customerDtoMono) {
        return customerDtoMono.transform(RequestValidator.validate())
                .as(validRequest -> this.customerService.updateCustomer(id, validRequest))
        .switchIfEmpty(ApplicationException.customerNotFound(id));
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteCustomer(@PathVariable Integer id) {
        return this.customerService.deleteCustomerById(id)
                .filter(value -> value)
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .then();
    }

}
