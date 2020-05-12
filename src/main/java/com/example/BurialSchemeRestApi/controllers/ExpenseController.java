package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.models.Expense;
import com.example.BurialSchemeRestApi.models.TransactionType;
import com.example.BurialSchemeRestApi.repositories.DependantRepo;
import com.example.BurialSchemeRestApi.repositories.ExpenseRepo;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class ExpenseController {

    Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    MemberRepo memberRepo;
    @Autowired
    DependantRepo dependantRepo;
    @Autowired
    ExpenseRepo expenseRepo;
    @Autowired
    TransactionTypeRepo transactionTypeRepo;
    @Autowired
    private UtilClass util;

    @GetMapping("/expenses")
    public ResponseEntity<?> allExpenses() {

        Map<String, Object> m = new HashMap<String, Object>();
        m.put("message", "success");
        m.put("data", expenseRepo.findAll());
        return ResponseEntity.status(HttpStatus.OK).body(m);
    }

    @PostMapping("/addExpense")
    public ResponseEntity<?> addExpense(@RequestBody Map<String, Object> requestMap) {

        BigDecimal amount;
        String reason;
        Long type;

        if(requestMap.get("reason")==null)
            return util.responseUtil("Invalid Request Body");
        else {
            reason = requestMap.get("reason").toString();
        }
        if(requestMap.get("type") ==null) {
            return util.responseUtil("Invalid Request Body");
        }else {
            type = Long.parseLong(requestMap.get("type").toString());
        }
        if(requestMap.get("amount") ==null) {
            return util.responseUtil("Invalid Request Body");
        }else {
            amount = new BigDecimal(requestMap.get("amount").toString()).setScale(2, RoundingMode.HALF_EVEN);
        }

        try{

            TransactionType t = transactionTypeRepo.findById(type).orElseThrow();
            Expense expense = new Expense();

            expense.setAmount(amount);
            expense.setReason(reason);
            expense.setTransactionType(t);

            Map<String, Object> m = new HashMap<String, Object> ();
            m.put("message", "success");
            m.put("data", expenseRepo.save(expense));
            return ResponseEntity.status(HttpStatus.OK).body(m);
        }catch (Exception ex){

            logger.error("No such transaction type");
            return util.responseUtil("No such transaction type");

        }


    }

}
