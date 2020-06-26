package com.example.BurialSchemeRestApi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimDTO {

    @NonNull private long ID;
    @NonNull private BigDecimal amount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
    private Date claimDate = new Date(System.currentTimeMillis());
    @NonNull private Date deathDate;
    @NonNull private Date buriedDate;
    @NonNull private String burialPlace;
    @NonNull private long transactionType;

}
