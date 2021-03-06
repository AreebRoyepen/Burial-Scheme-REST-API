package com.example.BurialSchemeRestApi.services;

import com.example.BurialSchemeRestApi.api.Message;
import com.example.BurialSchemeRestApi.api.ResponseMessageList;
import com.example.BurialSchemeRestApi.api.ResponseMessageObject;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.models.Income;
import com.example.BurialSchemeRestApi.models.TransactionType;
import com.example.BurialSchemeRestApi.repositories.IncomeRepo;
import com.example.BurialSchemeRestApi.repositories.TransactionTypeRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NoSuchElementException;

@Service
@Log4j2
public class IncomeService {

    IncomeRepo incomeRepo;
    TransactionTypeRepo transactionTypeRepo;

    public IncomeService(IncomeRepo incomeRepo, TransactionTypeRepo transactionTypeRepo) {
        this.incomeRepo = incomeRepo;
        this.transactionTypeRepo = transactionTypeRepo;
    }

    public Message allIncomes() {
        return ResponseMessageList.builder().data(incomeRepo.findAll()).message(ResponseStatus.SUCCESS.name()).build();
    }

    public Message addIncome(BigDecimal amount, Long type) throws ValidationException {

        try{

            TransactionType t = transactionTypeRepo.findById(type).orElseThrow();

            amount = amount.setScale(2, RoundingMode.HALF_EVEN);

            Income income = new Income();

            income.setAmount(amount);
            income.setTransactionType(t);

            return ResponseMessageObject.builder().data(incomeRepo.save(income)).message(ResponseStatus.SUCCESS.name()).build();
        }catch (NoSuchElementException ex){

            log.error("No such transaction type");
            throw new ValidationException("No such transaction type");

        }catch(Exception e){
            e.printStackTrace();
            throw new ValidationException(e.getMessage());
        }

    }

}
