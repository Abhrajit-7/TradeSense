package com.funWithStocks.FunWithStocks.ExceptionHandling;

public class DataFetchException extends RuntimeException {
    public DataFetchException(String message) {
        super(message);
    }
}

