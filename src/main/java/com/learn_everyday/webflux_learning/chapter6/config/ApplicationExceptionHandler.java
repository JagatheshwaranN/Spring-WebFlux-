package com.learn_everyday.webflux_learning.chapter6.config;

import com.learn_everyday.webflux_learning.chapter6.exception.CustomerNotFoundException;
import com.learn_everyday.webflux_learning.chapter6.exception.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Consumer;

@Service
public class ApplicationExceptionHandler {

    public Mono<ServerResponse> handleException(CustomerNotFoundException exception, ServerRequest serverRequest) {
        return handleException(HttpStatus.NOT_FOUND, exception, serverRequest, problemDetail ->  {
            problemDetail.setType(URI.create("http://localhost:8080/problems/customer-not-found"));
            problemDetail.setTitle("Customer Not Found");
        });
    }

    public Mono<ServerResponse> handleException(InvalidInputException exception, ServerRequest serverRequest) {
        return handleException(HttpStatus.BAD_REQUEST, exception, serverRequest, problemDetail -> {
            problemDetail.setType(URI.create("http://localhost:8080/problems/invalid-input"));
            problemDetail.setTitle("Invalid Input");
        });
    }

    private Mono<ServerResponse> handleException(HttpStatus status, Exception exception, ServerRequest serverRequest, Consumer<ProblemDetail> problemDetailConsumer) {
        var problem = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
        problemDetailConsumer.accept(problem);
        problem.setInstance(URI.create(serverRequest.path()));
        return ServerResponse.status(status).bodyValue(problem);
    }

}
