package com.funWithStocks.FunWithStocks.controller;

import com.funWithStocks.FunWithStocks.services.ProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class ProfitController {

    @Autowired
    private ProfitService profitService;

    @GetMapping("/realizedProfit")
    public BigDecimal getRealizedProfit(@RequestParam String stockCode, @RequestParam int quantity, @RequestParam BigDecimal buyPrice) {
        return profitService.calculateRealizedProfit(stockCode, quantity, buyPrice);
    }

    @GetMapping("/unrealizedProfit")
    public BigDecimal getUnrealizedProfit(@RequestParam String stockCode, @RequestParam int quantity, @RequestParam BigDecimal buyPrice) {
        return profitService.calculateUnrealizedProfit(stockCode, quantity, buyPrice);
    }
}

