package com.funWithStocks.FunWithStocks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WinnerDTO {

   private String username;
   private double totalInvested;
   private String selectedNumbers;

}
