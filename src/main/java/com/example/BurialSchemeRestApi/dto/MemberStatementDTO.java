package com.example.BurialSchemeRestApi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class MemberStatementDTO {

    Date date;
    String desc;
    BigDecimal amount;
    BigDecimal balance;

}
