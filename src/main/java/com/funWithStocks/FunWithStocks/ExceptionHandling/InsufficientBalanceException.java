package com.funWithStocks.FunWithStocks.ExceptionHandling;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
