package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.models.*;
import com.example.BurialSchemeRestApi.repositories.*;
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

@RestController
@CrossOrigin
public class ClaimController {

    Logger logger = LoggerFactory.getLogger(ClaimController.class);

    @Autowired
    MemberRepo memberRepo;
    @Autowired
    DependantRepo dependantRepo;
    @Autowired
    ClaimRepo claimRepo;
    @Autowired
    PremiumRepo premiumRepo;
    @Autowired
    TransactionTypeRepo transactionTypeRepo;
    @Autowired
    private UtilClass util;

    @GetMapping("/claims")
    public ResponseEntity<?> allClaims() {

        Map<String, Object> m = new HashMap<String, Object> ();
        m.put("message", "success");
        m.put("data", claimRepo.findAll());
        return ResponseEntity.status(HttpStatus.OK).body(m);
    }


    @PostMapping("/claim")
    public ResponseEntity<?> claim(@RequestBody Map<String, Object> requestMap) {

        Long id;
        Long type;
        BigDecimal amount;

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


        Map<String, Object> m = new HashMap<>();
        try {
            Member member =memberRepo.findById(id).orElseThrow();

            if(member.hasClaimed()){
                m.put("message", "Cannot claim more than once");
                return ResponseEntity.status(HttpStatus.OK).body(m);
            }else{

                try{

                    TransactionType t = transactionTypeRepo.findById(type).orElseThrow();
                    Claim claim = new Claim();

                    BigDecimal total = util.getBalanceAtDate(member, new Date(System.currentTimeMillis())).setScale(2, RoundingMode.HALF_EVEN);

                    if(total.compareTo(BigDecimal.ZERO) == 0){
                        return util.responseUtil("No funds");
                    }

                    if(total.compareTo(BigDecimal.ZERO) < 0){
                        return util.responseUtil("Negative Balance");
                    }

                    if(amount.compareTo(total) > 0){
                        return util.responseUtil("Cannot claim more than total premiums paid");
                    }

                    if(member.getDependants().isEmpty() || util.allDependantsClaimed(member)){
                        claim.setAmount(total);
                        claim.setBuriedDate(new Date(System.currentTimeMillis()));
                        claim.setMember(member);
                        claim.setTransactionType(t);
                        m.put("info", "This is the last claim possible so all funds are paid out");
                        m.put("message", "success");
                        member.setClaimed(true);
                        memberRepo.save(member);
                        m.put("data", claimRepo.save(claim));
                        return ResponseEntity.status(HttpStatus.OK).body(m);
                    }

                    //TODO
                    claim.setAmount(amount);
                    claim.setBuriedDate(new Date(System.currentTimeMillis()));
                    claim.setMember(member);
                    claim.setTransactionType(t);
                    m.put("message", "success");
                    member.setClaimed(true);
                    memberRepo.save(member);
                    m.put("data", claimRepo.save(claim));
                    return ResponseEntity.status(HttpStatus.OK).body(m);

                }catch (Exception ex){

                    logger.error("No such transaction type");
                    return util.responseUtil("No such transaction type");

                }

            }

        }catch(Exception e) {

            logger.error("Trying to claim from a member that does not exist");
            m.put("message", "No such member");
            return ResponseEntity.status(HttpStatus.OK).body(m);

        }
    }

    @PostMapping("/claimForDependant")
    public ResponseEntity<?> claimForDep(@RequestBody Map<String, Object> requestMap) {

        Long id;
        Long d;
        Long type;
        BigDecimal amount;

        if(requestMap.get("id")==null)
            return util.responseUtil("Invalid Request Body");
        else {
            id = Long.parseLong(requestMap.get("id").toString());
        }
        if(requestMap.get("d")==null)
            return util.responseUtil("Invalid Request Body");
        else {
            d = Long.parseLong(requestMap.get("d").toString());
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
        Map<String, Object> m = new HashMap<> ();

        try {
            Member member =memberRepo.findById(id).orElseThrow();

            try{
                Dependant dep = dependantRepo.findById(d).orElseThrow();
                if(dep.hasClaimed()){
                    m.put("message", "Cannot claim more than once");
                    return ResponseEntity.status(HttpStatus.OK).body(m);
                }else{
                    if(dep.getMember().getID() != member.getID()){
                        m.put("message", "ID's do not match");
                        return ResponseEntity.status(HttpStatus.OK).body(m);
                    }
                    try{

                        TransactionType t = transactionTypeRepo.findById(type).orElseThrow();
                        Claim claim = new Claim();


                        BigDecimal total = util.getBalanceAtDate(member, new Date(System.currentTimeMillis())).setScale(2, RoundingMode.HALF_EVEN);

                        if(amount.compareTo(total) > 0){
                            return util.responseUtil("Cannot claim more than total premiums paid");
                        }

                        if((member.getDependants().size() == 1 || util.lastDependantToClaim(member)) && member.hasClaimed()){
                            claim.setAmount(total);
                            claim.setBuriedDate(new Date(System.currentTimeMillis()));
                            claim.setMember(member);
                            claim.setTransactionType(t);
                            m.put("info", "This is the last claim possible so all funds are paid out");
                            m.put("message", "success");
                            dep.setClaimed(true);
                            dependantRepo.save(dep);
                            m.put("data", claimRepo.save(claim));
                            return ResponseEntity.status(HttpStatus.OK).body(m);
                        }


                        claim.setAmount(amount);
                        claim.setBuriedDate(new Date(System.currentTimeMillis()));
                        claim.setMember(member);
                        claim.setTransactionType(t);
                        m.put("message", "success");
                        dep.setClaimed(true);
                        dependantRepo.save(dep);
                        m.put("data", claimRepo.save(claim));
                        return ResponseEntity.status(HttpStatus.OK).body(m);

                    }catch (Exception ex){

                        logger.error("No such transaction type");
                        return util.responseUtil("No such transaction type");

                    }


                }

            }catch (Exception ex){
                logger.error("Trying to claim from a dependant that does not exist");
                m.put("message", "No such dependant");
                return ResponseEntity.status(HttpStatus.OK).body(m);
            }



        }catch(Exception e) {

            logger.error("Member that does not exist");
            m.put("message", "No such member");
            return ResponseEntity.status(HttpStatus.OK).body(m);

        }
    }

}
