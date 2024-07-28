package com.funWithStocks.FunWithStocks.controller;

import com.funWithStocks.FunWithStocks.dto.StockTransactionDTO;
import com.funWithStocks.FunWithStocks.entity.HistoricalStockPrice;
import com.funWithStocks.FunWithStocks.services.HistoricalStockPriceService;
import com.funWithStocks.FunWithStocks.services.PnLAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class HistoricalStockPriceController {

    @Autowired
    private PnLAnalysisService pnLAnalysisService;

    @Autowired
    private HistoricalStockPriceService historicalStockPriceService;

    @GetMapping("/fetchHistoricalData")
    public String fetchHistoricalData() {
        List<String> nseCodes = List.of(
                "RELIANCE", "TCS", "HDFCBANK", "INFY"//, "HINDUNILVR", "ICICIBANK", "ITC", "AXISBANK", "BHARTIARTL", "KOTAKBANK",
                //"NESTLEIND", "BAJFINANCE", "MARUTI", "HCLTECH", "SBIN", "LT", "ASIANPAINT", "ULTRACEMCO", "WIPRO", "DMART",
                //"POWERGRID", "SUNPHARMA", "TITAN", "NTPC", "TATASTEEL", "M&M", "ONGC", "JSWSTEEL", "HDFCLIFE", "DIVISLAB",
                //"BAJAJFINSV", "TATAMOTORS", "ADANIGREEN", "ADANIPORTS", "PIDILITIND", "GRASIM", "BPCL", "COALINDIA", "TECHM", "HINDALCO",
                //"HEROMOTOCO", "UPL", "HAVELLS", "CIPLA", "ADANITRANS", "TATAPOWER", "DLF", "SIEMENS", "APOLLOHOSP", "ICICIGI",
                //"SRTRANSFIN", "MCDOWELL-N", "BIOCON", "PEL", "INDIGO", "SBICARD", "BANDHANBNK", "CHOLAFIN", "NAUKRI", "ADANIENT",
                //"BANKBARODA", "IDFCFIRSTB", "ZOMATO", "BAJAJ-AUTO", "IGL", "NMDC", "ACC", "AUROPHARMA", "BERGEPAINT", "MANAPPURAM",
                //"BOSCHLTD", "AMBUJACEM", "HINDPETRO", "MOTHERSUMI", "ASHOKLEY", "INDUSINDBK", "LUPIN", "ICICIPRULI", "TRENT", "TORNTPHARM",
                //"LICI", "ABFRL", "COFORGE", "GODREJCP", "GLAND", "JUBLFOOD", "MPHASIS", "AARTIIND", "EXIDEIND", "ADANIPOWER",
                //"GAIL", "LALPATHLAB", "ADANIGAS", "INDHOTEL", "TATACOMM", "ESCORTS", "PNB", "CANBK", "VEDL", "BHEL"
        );

        historicalStockPriceService.fetchAndStoreHistoricalData(nseCodes);
        return "Historical data fetched and stored successfully!";
    }

    @GetMapping("/getHistoricalData")
    public List<HistoricalStockPrice> getHistoricalData(@RequestParam String stockCode) {
        return historicalStockPriceService.getHistoricalData(stockCode);
    }

    @GetMapping("/getP&LAnalysis")
    public ResponseEntity<List<StockTransactionDTO>> getMaxProfit(@RequestParam("stockCode") String stockCode) {
        BigDecimal maxProfitPrevWeek=pnLAnalysisService.maxProfitWithUnlimitedTransactions(stockCode);
        List<StockTransactionDTO> bestTimeToBuyNSell=pnLAnalysisService.findBestBuySellDays(stockCode);
        return ResponseEntity.ok(bestTimeToBuyNSell);
    }
}

