package com.learn_everyday.webflux_learning.chapter4.exception;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message) {
        super(message);
    }

}
