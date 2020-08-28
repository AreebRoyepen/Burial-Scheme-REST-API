package com.example.BurialSchemeRestApi.util;

import com.example.BurialSchemeRestApi.dto.DependantDTO;
import com.example.BurialSchemeRestApi.models.*;
import com.example.BurialSchemeRestApi.repositories.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UtilClass {

    PremiumRepo premiumRepo;
    ClaimRepo claimRepo;
    MemberRepo memberRepo;
    IncomeRepo incomeRepo;
    ExpenseRepo expenseRepo;
    DependantRepo dependantRepo;

    public UtilClass(PremiumRepo premiumRepo, ClaimRepo claimRepo, MemberRepo memberRepo, IncomeRepo incomeRepo, ExpenseRepo expenseRepo, DependantRepo dependantRepo) {
        this.premiumRepo = premiumRepo;
        this.claimRepo = claimRepo;
        this.memberRepo = memberRepo;
        this.incomeRepo = incomeRepo;
        this.expenseRepo = expenseRepo;
        this.dependantRepo = dependantRepo;
    }

    public BigDecimal getBalanceAtDate(Member member, Date date){

        List<Premium> premiums = premiumRepo.findByMemberAndDateLessThanEqual(member,date);
        List<Claim> claims = claimRepo.findByMemberAndClaimDateLessThanEqual(member,date);

        BigDecimal total = premiums.stream()
                .map(x -> x.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_EVEN);

        total = claims.stream()
                .map(x -> x.getAmount())
                .reduce(total, BigDecimal::min).setScale(2, RoundingMode.HALF_EVEN);

        return total;

    }

    public boolean allDependantsClaimed(Member member){
        List<DependantDTO> dependantList = member.getDependants();

        int x = 0;
        for (DependantDTO dependant: dependantList) {

            Dependant d  = dependantRepo.findById(dependant.getId()).orElseThrow();

            if(d.isClaimed())
                x ++;
        }
        if(x == dependantList.size()) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean lastDependantToClaim(Member member){
        List<DependantDTO> dependantList = member.getDependants();

        int x = 0;
        for (DependantDTO dependant: dependantList) {
            Dependant d  = dependantRepo.findById(dependant.getId()).orElseThrow();
            if(d.isClaimed())
                x ++;
        }
        if(x == dependantList.size() -1) {
            return true;
        }
        else {
            return false;
        }
    }

    public String formatDate(Date date){
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    public Map getDashboardDetails(){


        //premium balance
        BigDecimal premiums = premiumRepo.findAll().stream()
                    .map(x -> x.getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_EVEN);

        //claim balance
        BigDecimal claims = claimRepo.findAll().stream()
                    .map(x -> x.getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_EVEN);
        //}

        //income balance
        BigDecimal income = incomeRepo.findAll().stream()
                    .map(x -> x.getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_EVEN);

        //expense balance
        BigDecimal expenses = expenseRepo.findAll().stream()
                    .map(x -> x.getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_EVEN);

        int members = memberRepo.findAll().size();

        Map<Object, Object> map = new HashMap<>();

        map.put("premiums", premiums);
        map.put("claims", claims);
        map.put("members",members);
        map.put("incomes", income);
        map.put("expenses", expenses);

        return map;

    }
}
