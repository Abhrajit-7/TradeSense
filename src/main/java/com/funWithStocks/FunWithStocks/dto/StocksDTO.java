package com.funWithStocks.FunWithStocks.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StocksDTO {
    private Long bet_number;
    private int numberCount;
    private double bet_amount;
    private String selected_stock_nse_codes;
    private List<Double> selected_stock_prices;
    private List<Integer> selected_stock_quantities;
    private String username;
    private String slot;
}
