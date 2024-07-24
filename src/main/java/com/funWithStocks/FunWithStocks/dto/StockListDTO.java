package com.funWithStocks.FunWithStocks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockListDTO {

    private Long id;
    private String selected_stocks;
    private double amount;
}
