package com.example.BurialSchemeRestApi.dto;

import java.math.BigDecimal;
import java.text.ParseException;
import java.sql.Date;

public class MemberStatementDTO {

    Date date;
    String desc;
    BigDecimal amount;
    BigDecimal balance;

    public MemberStatementDTO(Date date, String desc, BigDecimal amount, BigDecimal balance) {
        this.date = date;
        this.desc = desc;
        this.amount = amount;
        this.balance = balance;
    }

    public String getDate() {
        return date.toString();
    }

    public void setDate(Date date) throws ParseException {
        this.date =  date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "MemberStatementDTO{" +
                "date=" + date +
                ", desc='" + desc + '\'' +
                ", amount=" + amount +
                ", balance=" + balance +
                '}';
    }
}
