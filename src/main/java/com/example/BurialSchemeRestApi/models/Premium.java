package com.example.BurialSchemeRestApi.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.sql.Date;

@Entity
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
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
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

    @Override
    public String toString() {
        return "Premium{" +
                "ID=" + ID +
                ", amount=" + amount +
                ", date=" + date +
                ", transactionType=" + transactionType +
                ", member=" + member +
                '}';
    }
}
