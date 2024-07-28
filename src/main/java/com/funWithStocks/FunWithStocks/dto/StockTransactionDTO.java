package com.funWithStocks.FunWithStocks.dto;

public class StockTransactionDTO {
    private final int buyDay;
    private final int sellDay;

    public StockTransactionDTO(int buyDay, int sellDay) {
        this.buyDay = buyDay;
        this.sellDay = sellDay;
    }

    public int getBuyDay() {
        return buyDay;
    }

    public int getSellDay() {
        return sellDay;
    }

    @Override
    public String toString() {
        return "Buy on day " + buyDay + ", sell on day " + sellDay;
    }
}