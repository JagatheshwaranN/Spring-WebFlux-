package com.learneveryday.customerportfolio.exceptions;

public class CustomerNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Customer id %d is not found";

    CustomerNotFoundException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}
