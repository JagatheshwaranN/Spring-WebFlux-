package com.learn_everyday.webflux_learning.chapter5.exception;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message) {
        super(message);
    }

}
