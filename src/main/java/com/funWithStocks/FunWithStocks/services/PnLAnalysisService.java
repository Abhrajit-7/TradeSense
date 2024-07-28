package com.funWithStocks.FunWithStocks.services;

import com.funWithStocks.FunWithStocks.dto.StockTransactionDTO;
import com.funWithStocks.FunWithStocks.entity.HistoricalStockPrice;
import com.funWithStocks.FunWithStocks.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PnLAnalysisService {

    @Autowired
    private HistoricalStockPriceService historicalStockPriceService;
    @Autowired
    private StockRepository stockRepository;

    public BigDecimal maxProfitWithUnlimitedTransactions(String stockCode) {
        List<HistoricalStockPrice> prices = historicalStockPriceService.getHistoricalData(stockCode);
        if (prices.isEmpty()) {
            return BigDecimal.ZERO;
        }

        int n = prices.size();
        BigDecimal[][] dp = new BigDecimal[n][2];

        // Initialize dp array
        dp[0][0] = BigDecimal.ZERO; // No stock on the first day
        dp[0][1] = BigDecimal.valueOf(-Double.MAX_VALUE); // Impossible to hold stock initially

        for (int i = 1; i < n; i++) {
            BigDecimal priceToday = prices.get(i).getPrice();
            BigDecimal priceYesterday = prices.get(i - 1).getPrice();

            // Update dp values
            dp[i][0] = dp[i - 1][0].max(dp[i - 1][1].add(priceToday)); // Max profit not holding stock today
            dp[i][1] = dp[i - 1][1].max(dp[i - 1][0].subtract(priceToday)); // Max profit holding stock today
        }

        // The maximum profit we can achieve is when we do not hold any stock on the last day
        return dp[n - 1][0];
    }

    public List<StockTransactionDTO> findBestBuySellDays(String stockCode) {
        List<HistoricalStockPrice> prices = historicalStockPriceService.getHistoricalData(stockCode);
        if (prices.isEmpty()) {
            return new ArrayList<>();
        }

        int n = prices.size();
        BigDecimal minPrice = prices.get(0).getPrice();
        int minPriceDay = 0;

        BigDecimal maxProfit = BigDecimal.ZERO;
        int buyDay = 0;
        int sellDay = 0;

        for (int i = 1; i < n; i++) {
            BigDecimal priceToday = prices.get(i).getPrice();

            // Calculate profit if selling on the current day
            BigDecimal profit = priceToday.subtract(minPrice);

            // Update max profit and corresponding buy/sell days
            if (profit.compareTo(maxProfit) > 0) {
                maxProfit = profit;
                buyDay = minPriceDay;
                sellDay = i;
            }

            // Update min price and corresponding day
            if (priceToday.compareTo(minPrice) < 0) {
                minPrice = priceToday;
                minPriceDay = i;
            }
        }

        List<StockTransactionDTO> transactions = new ArrayList<>();
        if (maxProfit.compareTo(BigDecimal.ZERO) > 0) {
            transactions.add(new StockTransactionDTO(buyDay, sellDay));
        }

        System.out.println("Max Profit: " + maxProfit);
        return transactions;
    }
}