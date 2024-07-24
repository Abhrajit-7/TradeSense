package com.funWithStocks.FunWithStocks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CallBackDTO {
    private Long transactionId;
    private String paymentStatus;
    private String username;
    private double amount;
}
