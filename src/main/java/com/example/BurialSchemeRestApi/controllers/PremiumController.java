package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.models.Premium;
import com.example.BurialSchemeRestApi.models.TransactionType;
import com.example.BurialSchemeRestApi.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class PremiumController {

    Logger logger = LoggerFactory.getLogger(PremiumController.class);

    @Autowired
    PremiumRepo premiumRepo;
    @Autowired
    MemberRepo memberRepo;
    @Autowired
    DependantRepo dependantRepo;
    @Autowired
    TransactionTypeRepo transactionTypeRepo;
    @Autowired
    private UtilController util;

    @GetMapping("/premiums")
    public ResponseEntity<?> allPremiums() {

        Map<String, Object> m = new HashMap<String, Object>();
        m.put("message", "success");
        m.put("data", premiumRepo.findAll());
        return ResponseEntity.status(HttpStatus.OK).body(m);
    }

    @PostMapping("/addExpense")
    public ResponseEntity<?> addExpense(@RequestBody Map<String, Object> requestMap) {

        BigDecimal amount;
        Long id;
        Long type;

        if(requestMap.get("id")==null)
            return util.responseUtil("Invalid Request Body");
        else {
            id = Long.parseLong(requestMap.get("id").toString());
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

            try {
                Premium premium = new Premium();
                premium.setAmount(amount);
                premium.setDate(new Date(System.currentTimeMillis()));
                premium.setMember(memberRepo.findById(id).orElseThrow());
                premium.setTransactionType(t);

                Map<String, Object> m = new HashMap<String, Object> ();
                m.put("message", "success");
                m.put("data", premiumRepo.save(premium));
                return ResponseEntity.status(HttpStatus.OK).body(m);


            }catch (Exception ex){
                logger.error("No such Member");
                return util.responseUtil("No such Member");
            }


        }catch (Exception ex){

            logger.error("No such transaction type");
            return util.responseUtil("No such transaction type");

        }


    }


}
