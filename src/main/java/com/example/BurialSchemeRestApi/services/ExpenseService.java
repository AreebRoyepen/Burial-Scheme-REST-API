package com.example.BurialSchemeRestApi.services;

import com.example.BurialSchemeRestApi.api.Message;
import com.example.BurialSchemeRestApi.api.ResponseMessageList;
import com.example.BurialSchemeRestApi.api.ResponseMessageObject;
import com.example.BurialSchemeRestApi.dto.ExpenseDTO;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.models.Expense;
import com.example.BurialSchemeRestApi.models.TransactionType;
import com.example.BurialSchemeRestApi.repositories.DependantRepo;
import com.example.BurialSchemeRestApi.repositories.ExpenseRepo;
import com.example.BurialSchemeRestApi.repositories.MemberRepo;
import com.example.BurialSchemeRestApi.repositories.TransactionTypeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

@Service
public class ExpenseService {
    Logger logger = LoggerFactory.getLogger(ExpenseService.class);

    MemberRepo memberRepo;
    DependantRepo dependantRepo;
    ExpenseRepo expenseRepo;
    TransactionTypeRepo transactionTypeRepo;

    public ExpenseService(MemberRepo memberRepo, DependantRepo dependantRepo, ExpenseRepo expenseRepo,
                          TransactionTypeRepo transactionTypeRepo) {
        this.memberRepo = memberRepo;
        this.dependantRepo = dependantRepo;
        this.expenseRepo = expenseRepo;
        this.transactionTypeRepo = transactionTypeRepo;
    }

    public Message allExpenses() {
        return ResponseMessageList.builder().data(expenseRepo.findAll()).message(ResponseStatus.SUCCESS.name()).build();
    }

    public Message addExpense(ExpenseDTO expenseDTO) throws ValidationException {

        try{

            TransactionType t = transactionTypeRepo.findById(expenseDTO.getType()).orElseThrow();
            Expense expense = new Expense();

            expense.setAmount(expenseDTO.getAmount());
            expense.setReason(expenseDTO.getReason());
            expense.setTransactionType(t);

            return ResponseMessageObject.builder().data(expenseRepo.save(expense)).message(ResponseStatus.SUCCESS.name()).build();

        }catch (NoSuchElementException ex){

            logger.error("No such transaction type");
            throw new ValidationException("No such transaction type");

        }catch(Exception ex){
            ex.printStackTrace();
            throw new ValidationException(ex.getMessage());
        }


    }


}