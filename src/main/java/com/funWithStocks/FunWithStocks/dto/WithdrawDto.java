package com.funWithStocks.FunWithStocks.dto;

import com.funWithStocks.FunWithStocks.util.TransStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WithdrawDto {
    Long trans_id;
    Timestamp dateTime;
    TransStatus status;
    String fullName;
    String email;
    String pan;
    String bankAcctNumber;
    String ifscCode;
    double amount;
    String aadhaar;
}
