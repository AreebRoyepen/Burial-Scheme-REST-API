package com.example.BurialSchemeRestApi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.sql.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class Premium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private BigDecimal amount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
    private Date date =  new Date(System.currentTimeMillis());

    @ManyToOne
    @JoinColumn(name = "transactionTypeID" , nullable = false)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "memberID" , nullable = false)
    private Member member;

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

}
