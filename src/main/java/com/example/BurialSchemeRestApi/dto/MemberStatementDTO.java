package com.example.BurialSchemeRestApi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class MemberStatementDTO {

    String date;
    String desc;
    BigDecimal amount;
    BigDecimal balance;

}
