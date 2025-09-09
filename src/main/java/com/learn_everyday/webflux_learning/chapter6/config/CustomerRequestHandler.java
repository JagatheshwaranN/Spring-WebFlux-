package com.learn_everyday.webflux_learning.chapter6.config;

import com.learn_everyday.webflux_learning.chapter6.service.CustomerService;
import com.learn_everyday.webflux_learning.chapter6.dto.CustomerDto;
import com.learn_everyday.webflux_learning.chapter6.exception.ApplicationException;
import com.learn_everyday.webflux_learning.chapter6.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class CustomerRequestHandler {

    @Autowired
    CustomerService customerService;

    public Mono<ServerResponse> allCustomers(ServerRequest request) {
        //request.pathVariable();
        //request.headers();
        //request.queryParams();
        return this.customerService.getAllCustomers()
                .as(customerDtoFlux -> ServerResponse.ok().body(customerDtoFlux, CustomerDto.class));
    }

    public Mono<ServerResponse> getPaginatedCustomers(ServerRequest request) {
        var pageOffset = request.queryParam("pageOffset").map(Integer::parseInt).orElse(1);
        var size = request.queryParam("size").map(Integer::parseInt).orElse(1);
        return this.customerService.getAllCustomers(pageOffset, size)
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> getCustomer(ServerRequest request) {
        var id = Integer.parseInt(request.pathVariable("id"));
        return this.customerService.getCustomerById(id)
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        return request.bodyToMono(CustomerDto.class)
                .transform(RequestValidator.validate())
                .as(this.customerService::saveCustomer)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest request) {
        var id = Integer.parseInt(request.pathVariable("id"));
        return request.bodyToMono(CustomerDto.class)
                .transform(RequestValidator.validate())
                .as(validatedRequest -> this.customerService.updateCustomer(id, validatedRequest))
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
        var id = Integer.parseInt(request.pathVariable("id"));
        return this.customerService.deleteCustomerById(id)
                .filter(data -> data)
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .then(ServerResponse.ok().build());
    }

}
