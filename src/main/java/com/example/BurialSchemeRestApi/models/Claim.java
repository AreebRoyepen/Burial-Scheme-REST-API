package com.example.BurialSchemeRestApi.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private BigDecimal amount;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "transactionTypeID" , nullable = false)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "memberID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "dependantID")
    private Dependant dependant;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Dependant getDependant() {
        return dependant;
    }

    public void setDependant(Dependant dependant) {
        this.dependant = dependant;
    }
}
