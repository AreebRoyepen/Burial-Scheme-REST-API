package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.models.Income;
import com.example.BurialSchemeRestApi.models.TransactionType;
import com.example.BurialSchemeRestApi.repositories.DependantRepo;
import com.example.BurialSchemeRestApi.repositories.IncomeRepo;
import com.example.BurialSchemeRestApi.repositories.MemberRepo;
import com.example.BurialSchemeRestApi.repositories.TransactionTypeRepo;
import com.example.BurialSchemeRestApi.util.UtilClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
public class IncomeController {

    Logger logger = LoggerFactory.getLogger(IncomeController.class);

    @Autowired
    MemberRepo memberRepo;
    @Autowired
    DependantRepo dependantRepo;
    @Autowired
    IncomeRepo incomeRepo;
    @Autowired
    TransactionTypeRepo transactionTypeRepo;
    @Autowired
    private UtilClass util;

    @GetMapping("/income")
    public ResponseEntity<?> allExpenses() {

        Map<String, Object> m = new HashMap<String, Object>();
        m.put("message", "success");
        m.put("data", incomeRepo.findAll());
        return ResponseEntity.status(HttpStatus.OK).body(m);
    }

    @GetMapping("/addIncome")
    public ResponseEntity<?> addExpense(@PathVariable BigDecimal amount, @PathVariable Long type) {

        try{

            TransactionType t = transactionTypeRepo.findById(type).orElseThrow();

            amount.setScale(2, RoundingMode.HALF_EVEN);

            Income income = new Income();

            income.setAmount(amount);
            income.setTransactionType(t);

            Map<String, Object> m = new HashMap<String, Object> ();
            m.put("message", "success");
            m.put("data", incomeRepo.save(income));
            return ResponseEntity.status(HttpStatus.OK).body(m);
        }catch (NoSuchElementException ex){

            logger.error("No such transaction type");
            return util.responseUtil("No such transaction type");

        }catch(Exception e){
            e.printStackTrace();
            return util.responseUtil(e.getMessage());
        }

    }

}
