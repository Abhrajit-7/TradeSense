package com.funWithStocks.FunWithStocks;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import com.funWithStocks.FunWithStocks.controller.ProfitController;
import com.funWithStocks.FunWithStocks.repository.StockRepository;
import com.funWithStocks.FunWithStocks.services.ProfitService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class ProfitControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProfitService profitService;

    @InjectMocks
    private ProfitController profitController;

    @Autowired
    private StockRepository stockRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(profitController).build();
    }

    @Test
    public void testGetRealizedProfit() throws Exception {
        String stockCode = "AAPL";
        int quantity = 9;
        BigDecimal buyPrice = new BigDecimal("101.00");
        BigDecimal realizedProfit = new BigDecimal("50.00");

        when(profitService.calculateRealizedProfit(stockCode, quantity, buyPrice)).thenReturn(realizedProfit);

        mockMvc.perform(get("/realizedProfit")
                        .param("stockCode", stockCode)
                        .param("quantity", String.valueOf(quantity))
                        .param("buyPrice", buyPrice.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(realizedProfit.toString()));
    }

    @Test
    public void testGetUnrealizedProfit() throws Exception {
        String stockCode = "RELIANCE";
        int quantity = 10;
        BigDecimal buyPrice = stockRepository.findLatestPrice("RELIANCE");
        BigDecimal unrealizedProfit = new BigDecimal("70.00");

        when(profitService.calculateUnrealizedProfit(stockCode, quantity, buyPrice)).thenReturn(unrealizedProfit);

        mockMvc.perform(get("/unrealizedProfit")
                        .param("stockCode", stockCode)
                        .param("quantity", String.valueOf(quantity))
                        .param("buyPrice", buyPrice.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(unrealizedProfit.toString()));
    }
}

