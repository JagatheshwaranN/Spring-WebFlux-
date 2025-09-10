package com.learn_everyday.webflux_learning.chapter6.handson;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

import java.util.function.BiFunction;

@Configuration
public class CalculatorController {

    @Bean
    public RouterFunction<ServerResponse> calculatorRoute() {
        return RouterFunctions.route()
                .path("calculator", this::calculatorOperations)
                .build();
    }

    private RouterFunction<ServerResponse> calculatorOperations() {
        return RouterFunctions.route()
                .GET("/{a}/0", badRequest("b cannot be 0."))
                .GET("/{a}/{b}", isOperation("+"), handleRequest((a, b) -> a + b))
                .GET("/{a}/{b}", isOperation("-"), handleRequest((a, b) -> a - b))
                .GET("/{a}/{b}", isOperation("*"), handleRequest((a, b) -> a * b))
                .GET("/{a}/{b}", isOperation("/"), handleRequest((a, b) -> a / b))
                .GET("/{a}/{b}", badRequest("Operations header should be of +|-|*|/"))
                .build();
    }

    private RequestPredicate isOperation(String operations) {
        return RequestPredicates.headers(header ->
                operations.equals(header.firstHeader("operations")));
    }

    private HandlerFunction<ServerResponse> handleRequest(BiFunction<Integer, Integer, Integer> function) {
        return request -> {
            var a = Integer.parseInt(request.pathVariable("a"));
            var b = Integer.parseInt(request.pathVariable("b"));
            var result = function.apply(a, b);
            return ServerResponse.ok().bodyValue(result);
        };
    }

    private HandlerFunction<ServerResponse> badRequest(String message) {
        return request -> ServerResponse.badRequest().bodyValue(message);
    }

}
