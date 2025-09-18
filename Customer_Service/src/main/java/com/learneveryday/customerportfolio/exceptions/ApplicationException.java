package com.learneveryday.customerportfolio.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationException extends RuntimeException {

    public static <T>Mono<T> customerNotFound(Integer customerId) {
        return Mono.error(new CustomerNotFoundException(customerId));
    }

    public static <T>Mono<T> insufficientBalance(Integer customerId) {
        return Mono.error(new InsufficientBalanceException(customerId));
    }

    public static <T>Mono<T> insufficientShares(Integer customerId) {
        return Mono.error(new InsufficientSharesException(customerId));
    }

}
