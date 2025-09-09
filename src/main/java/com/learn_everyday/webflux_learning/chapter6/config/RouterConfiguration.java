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
        return RouterFunctions.route()
                .GET("/customers", this.customerRequestHandler::allCustomers)
                .GET("/customers/paginated", this.customerRequestHandler::getPaginatedCustomers)
                .GET("/customers/{id}", this.customerRequestHandler::getCustomer)
                .POST("/customers", this.customerRequestHandler::saveCustomer)
                .PUT("/customers/{id}", this.customerRequestHandler::updateCustomer)
                .DELETE("/customers/{id}", this.customerRequestHandler::deleteCustomer)
                .onError(CustomerNotFoundException.class, this.applicationExceptionHandler::handleException)
                .onError(InvalidInputException.class, this.applicationExceptionHandler::handleException)
                .build();
    }


// Web Filter to allow the request to pass on to next filter
//
//    @Bean
//    public RouterFunction<ServerResponse> customerRoutes1() {
//        return RouterFunctions.route()
//                .GET("/customers", this.customerRequestHandler::allCustomers)
//                .GET("/customers/paginated", this.customerRequestHandler::getPaginatedCustomers)
//                .GET("/customers/{id}", this.customerRequestHandler::getCustomer)
//                .POST("/customers", this.customerRequestHandler::saveCustomer)
//                .PUT("/customers/{id}", this.customerRequestHandler::updateCustomer)
//                .DELETE("/customers/{id}", this.customerRequestHandler::deleteCustomer)
//                .onError(CustomerNotFoundException.class, this.applicationExceptionHandler::handleException)
//                .onError(InvalidInputException.class, this.applicationExceptionHandler::handleException)
//                .filter(((request, next) -> {
//                    return next.handle(request);
//                }))
//                .filter(((request, next) -> {
//                    return next.handle(request);
//                }))
//                .build();
//    }

// Web Filter to reject the request
//
//    @Bean
//    public RouterFunction<ServerResponse> customerRoutes1() {
//        return RouterFunctions.route()
//                .GET("/customers", this.customerRequestHandler::allCustomers)
//                .GET("/customers/paginated", this.customerRequestHandler::getPaginatedCustomers)
//                .GET("/customers/{id}", this.customerRequestHandler::getCustomer)
//                .POST("/customers", this.customerRequestHandler::saveCustomer)
//                .PUT("/customers/{id}", this.customerRequestHandler::updateCustomer)
//                .DELETE("/customers/{id}", this.customerRequestHandler::deleteCustomer)
//                .onError(CustomerNotFoundException.class, this.applicationExceptionHandler::handleException)
//                .onError(InvalidInputException.class, this.applicationExceptionHandler::handleException)
//                .filter(((request, next) -> {
//                    return ServerResponse.badRequest().build();
//                }))
//                .build();
//    }

//    We can have nested router functions to handle the requests.
//    ===========================================================
//
//    @Bean
//    public RouterFunction<ServerResponse> customerRoutes() {
//        return RouterFunctions.route()
//                .path("customers", this::customerRoutesChild)
//                .POST("/customers", this.customerRequestHandler::saveCustomer)
//                .PUT("/customers/{id}", this.customerRequestHandler::updateCustomer)
//                .DELETE("/customers/{id}", this.customerRequestHandler::deleteCustomer)
//                .onError(CustomerNotFoundException.class, this.applicationExceptionHandler::handleException)
//                .onError(InvalidInputException.class, this.applicationExceptionHandler::handleException)
//                .build();
//    }
//
//    @Bean
//    public RouterFunction<ServerResponse> customerRoutesChild() {
//        return RouterFunctions.route()
//                .GET("/paginated", this.customerRequestHandler::getPaginatedCustomers)
//                .GET("/{id}", this.customerRequestHandler::getCustomer)
//                .GET( this.customerRequestHandler::allCustomers)
//                .build();
//    }

//    We can have multiple router functions to handle the requests.
//    =============================================================
//
//    @Bean
//    public RouterFunction<ServerResponse> customerRoutes1() {
//        return RouterFunctions.route()
//                .GET("/customers", this.customerRequestHandler::allCustomers)
//                .GET("/customers/paginated", this.customerRequestHandler::getPaginatedCustomers)
//                .GET("/customers/{id}", this.customerRequestHandler::getCustomer)
//                .onError(CustomerNotFoundException.class, this.applicationExceptionHandler::handleException)
//                .onError(InvalidInputException.class, this.applicationExceptionHandler::handleException)
//                .build();
//    }
//
//    @Bean
//    public RouterFunction<ServerResponse> customerRoutes2() {
//        return RouterFunctions.route()
//                .POST("/customers", this.customerRequestHandler::saveCustomer)
//                .PUT("/customers/{id}", this.customerRequestHandler::updateCustomer)
//                .DELETE("/customers/{id}", this.customerRequestHandler::deleteCustomer)
//                .onError(CustomerNotFoundException.class, this.applicationExceptionHandler::handleException)
//                .onError(InvalidInputException.class, this.applicationExceptionHandler::handleException)
//                .build();
//    }

}
