package com.learn_everyday.webflux_learning.chapter6.config;

import com.learn_everyday.webflux_learning.chapter6.exception.CustomerNotFoundException;
import com.learn_everyday.webflux_learning.chapter6.exception.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    @Autowired
    CustomerRequestHandler customerRequestHandler;

    @Autowired
    ApplicationExceptionHandler applicationExceptionHandler;

    @Bean
    public RouterFunction<ServerResponse> customerRoutes() {
        RouterFunctions.route()
                .GET("/customers", this.customerRequestHandler::allCustomers)
                .GET("/customers/{id}", this.customerRequestHandler::getCustomer)
                .POST("/customers", this.customerRequestHandler::saveCustomer)
                .PUT("/customers/{id}", this.customerRequestHandler::updateCustomer)
                .DELETE("/customers/{id}", this.customerRequestHandler::deleteCustomer)
                .onError(CustomerNotFoundException.class, this.applicationExceptionHandler::handleException)
                .onError(InvalidInputException.class, this.applicationExceptionHandler::handleException)
                .build();
    }

}
