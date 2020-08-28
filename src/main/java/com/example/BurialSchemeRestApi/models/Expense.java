package com.example.BurialSchemeRestApi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private BigDecimal amount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
    private String reason;

    @ManyToOne
    @JoinColumn(name = "transactionTypeID" , nullable = false)
    private TransactionType transactionType;

}
