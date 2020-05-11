package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.models.Dependant;
import com.example.BurialSchemeRestApi.models.Member;
import com.example.BurialSchemeRestApi.models.Premium;
import com.example.BurialSchemeRestApi.models.TransactionType;
import com.example.BurialSchemeRestApi.repositories.PremiumRepo;
import com.example.BurialSchemeRestApi.repositories.TransactionTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin
public class UtilController {

    @Autowired
    PremiumRepo premiumRepo;


    public ResponseEntity<?> responseUtil(String message) {
        Map<String, String> m = new HashMap<String, String>();
        m.put("message", message);
        return ResponseEntity.status(HttpStatus.OK).body(m);
    }

    public BigDecimal getTotalPremiums (Member member){

        List<Premium> list = premiumRepo.findByMember(member);
        BigDecimal total = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
        for (Premium p : list ) {
            total.add(p.getAmount());
        }
        return total;

    }

    public boolean allDependantsClaimed(Member member){
        Set<Dependant> set = member.getDependants();

        int x = 0;
        for (Dependant dependant: set) {
            if(dependant.hasClaimed())
                x ++;
        }
        if(x == set.size()) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean lastDependantToClaim(Member member){
        Set<Dependant> set = member.getDependants();

        int x = 0;
        for (Dependant dependant: set) {
            if(dependant.hasClaimed())
                x ++;
        }
        if(x == set.size() -1) {
            return true;
        }
        else {
            return false;
        }
    }

}
