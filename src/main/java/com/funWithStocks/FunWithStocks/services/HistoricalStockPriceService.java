package com.funWithStocks.FunWithStocks.services;

import com.funWithStocks.FunWithStocks.entity.HistoricalStockPrice;
import com.funWithStocks.FunWithStocks.repository.HistoricalStockPriceRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistoricalStockPriceService {

    @Value("${alphavantage.api.key}")
    private String apiKey;


    @Autowired
    private HistoricalStockPriceRepository historicalStockPriceRepository;

    public void fetchAndStoreHistoricalData(List<String> nseCodes) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        RestTemplate restTemplate = new RestTemplate();
        for (String nseCode : nseCodes) {
            try {
                String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + nseCode +".BSE&apikey=" + apiKey;
                String response = restTemplate.getForObject(url, String.class);

                // Log the response for debugging
                System.out.println("Response for " + nseCode + ": " + response);

                JSONObject jsonResponse = new JSONObject(response);

                if (jsonResponse.has("Time Series (Daily)")) {
                    JSONObject timeSeries = jsonResponse.getJSONObject("Time Series (Daily)");
                    List<HistoricalStockPrice> historicalPrices = new ArrayList<>();
                    for (String date : timeSeries.keySet()) {
                        LocalDate quoteDate = LocalDate.parse(date, formatter);
                        if (!quoteDate.isBefore(startDate) && !quoteDate.isAfter(endDate)) {
                            JSONObject quote = timeSeries.getJSONObject(date);
                            HistoricalStockPrice historicalStockPrice = new HistoricalStockPrice();
                            historicalStockPrice.setStockCode(nseCode);
                            historicalStockPrice.setPrice(BigDecimal.valueOf(quote.getDouble("4. close")));
                            historicalStockPrice.setDate(quoteDate);
                            historicalPrices.add(historicalStockPrice);
                        }
                    }
                    historicalStockPriceRepository.saveAll(historicalPrices);
                } else {
                    // Handle the case where the "Time Series (Daily)" key is missing
                    System.err.println("Time Series data not available for stock code: " + nseCode);
                }

            } catch (JSONException e) {
                // Handle JSON parsing exceptions
                System.err.println("Failed to parse JSON for stock code: " + nseCode);
                e.printStackTrace();
            } catch (Exception e) {
                // Handle other exceptions
                System.err.println("Failed to fetch data for stock code: " + nseCode);
                e.printStackTrace();
            }
        }
    }

    public List<HistoricalStockPrice> getHistoricalData(String nseCode) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);
        return historicalStockPriceRepository.findByStockCodeAndDateBetween(nseCode, startDate, endDate);
    }
}

