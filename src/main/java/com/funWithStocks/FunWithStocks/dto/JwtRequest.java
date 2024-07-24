package com.funWithStocks.FunWithStocks.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtRequest {

    String username;
    String password;
}
