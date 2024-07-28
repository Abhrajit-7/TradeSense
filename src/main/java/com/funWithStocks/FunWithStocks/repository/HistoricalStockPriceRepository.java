package com.funWithStocks.FunWithStocks.repository;

import com.funWithStocks.FunWithStocks.entity.HistoricalStockPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HistoricalStockPriceRepository extends JpaRepository<HistoricalStockPrice, Long> {
    List<HistoricalStockPrice> findByStockCodeAndDateBetween(String stockCode, LocalDate startDate, LocalDate endDate);
}

