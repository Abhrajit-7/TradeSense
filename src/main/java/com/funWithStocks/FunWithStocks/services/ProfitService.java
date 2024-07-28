package com.funWithStocks.FunWithStocks.services;

import com.funWithStocks.FunWithStocks.ExceptionHandling.DataFetchException;
import com.funWithStocks.FunWithStocks.entity.HistoricalStockPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProfitService {

    @Autowired
    private HistoricalStockPriceService historicalStockPriceService;

    public BigDecimal calculateRealizedProfit(String stockCode, int quantity, BigDecimal buyPrice) {
        List<HistoricalStockPrice> historicalPrices = historicalStockPriceService.getHistoricalData(stockCode);

        if (historicalPrices.isEmpty()) {
            throw new DataFetchException("No historical data found for stock: " + stockCode);
        }

        BigDecimal currentPrice = historicalPrices.get(historicalPrices.size() - 1).getPrice();
        return currentPrice.subtract(buyPrice).multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal calculateUnrealizedProfit(String stockCode, int quantity, BigDecimal buyPrice) {
        List<HistoricalStockPrice> historicalPrices = historicalStockPriceService.getHistoricalData(stockCode);

        if (historicalPrices.isEmpty()) {
            throw new DataFetchException("No historical data found for stock: " + stockCode);
        }

        BigDecimal currentPrice = historicalPrices.get(historicalPrices.size() - 1).getPrice();
        return currentPrice.subtract(buyPrice).multiply(BigDecimal.valueOf(quantity));
    }
}

