package com.learneveryday.customerportfolio.exceptions;

public class InsufficientBalanceException extends RuntimeException {

    private static final String MESSAGE = "Customer id %d does not have enough funds to complete the transaction";

    InsufficientBalanceException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}
