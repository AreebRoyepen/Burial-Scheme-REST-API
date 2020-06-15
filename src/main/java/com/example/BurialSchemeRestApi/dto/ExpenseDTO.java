package com.example.BurialSchemeRestApi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpenseDTO {

    @NonNull BigDecimal amount;
    @NonNull String reason;
    @NonNull Long type;

}
