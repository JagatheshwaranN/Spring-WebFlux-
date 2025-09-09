package com.learn_everyday.webflux_learning.chapter6.exception;

public class CustomerNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Customer id %d is not found";

    CustomerNotFoundException(Integer id) {
        super(MESSAGE.formatted(id));
    }

}
