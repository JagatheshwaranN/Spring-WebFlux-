package com.learneveryday.customerportfolio.advice;

import com.learneveryday.customerportfolio.exceptions.CustomerNotFoundException;
import com.learneveryday.customerportfolio.exceptions.InsufficientBalanceException;
import com.learneveryday.customerportfolio.exceptions.InsufficientSharesException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.util.function.Consumer;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleException(CustomerNotFoundException exception) {
        return build(HttpStatus.NOT_FOUND, exception, problemDetail -> {
            problemDetail.setType(URI.create("http://example.com/problems/customer-not-found"));
            problemDetail.setTitle("Customer Not Found");
        });
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ProblemDetail handleException(InsufficientBalanceException exception) {
        return build(HttpStatus.BAD_REQUEST, exception, problemDetail -> {
            problemDetail.setType(URI.create("http://example.com/problems/insufficient-balance"));
            problemDetail.setTitle("Insufficient Balance");
        });
    }

    @ExceptionHandler(InsufficientSharesException.class)
    public ProblemDetail handleException(InsufficientSharesException exception) {
        return build(HttpStatus.BAD_REQUEST, exception, problemDetail -> {
            problemDetail.setType(URI.create("http://example.com/problems/insufficient-shares"));
            problemDetail.setTitle("Insufficient Shares");
        });
    }

    private ProblemDetail build(HttpStatus status, Exception ex, Consumer<ProblemDetail> consumer) {
        var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        consumer.accept(problem);
        return problem;
    }

}
