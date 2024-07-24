package com.funWithStocks.FunWithStocks.ExceptionHandling;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(String message) {
        super(message);
    }
}

