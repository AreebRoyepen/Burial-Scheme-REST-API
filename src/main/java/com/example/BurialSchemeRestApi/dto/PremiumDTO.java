package com.example.BurialSchemeRestApi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PremiumDTO {
    @NonNull BigDecimal amount;
    @NonNull Long id;
    @NonNull Long type;
}
