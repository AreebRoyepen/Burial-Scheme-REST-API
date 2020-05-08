package com.example.BurialSchemeRestApi.models;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private BigDecimal amount;
    private String reason;

    @ManyToOne
    @JoinColumn(name = "transactionTypeID" , nullable = false)
    private TransactionType transactionType;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
