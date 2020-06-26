package com.example.BurialSchemeRestApi.reports;

import com.example.BurialSchemeRestApi.dto.MemberStatementDTO;
import com.example.BurialSchemeRestApi.models.Claim;
import com.example.BurialSchemeRestApi.models.Member;
import com.example.BurialSchemeRestApi.models.Premium;
import com.example.BurialSchemeRestApi.repositories.ClaimRepo;
import com.example.BurialSchemeRestApi.repositories.PremiumRepo;
import com.example.BurialSchemeRestApi.util.UtilClass;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
public class ReportData {

    ClaimRepo claimRepo;
    PremiumRepo premiumRepo;
    UtilClass utilClass;

    public ReportData(ClaimRepo claimRepo, PremiumRepo premiumRepo, UtilClass utilClass) {
        this.claimRepo = claimRepo;
        this.premiumRepo = premiumRepo;
        this.utilClass = utilClass;
    }


    public List getStatementList(Member m) throws ParseException {


        int year = Calendar.getInstance().get(Calendar.YEAR);

        Date balanceBroughtDownDate = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("31/01/"+year).getTime());

        BigDecimal balanceBroughtDown = utilClass.getBalanceAtDate(m, balanceBroughtDownDate);

        List<Claim> claims = claimRepo.findByMemberAndClaimDateAfter(m,balanceBroughtDownDate);
        List<Premium> premiums = premiumRepo.findByMemberAndDateAfter(m, balanceBroughtDownDate);
        List<MemberStatementDTO> mergedList = new ArrayList<>();

        BigDecimal balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
        int i = claims.size();
        int j = premiums.size();



        mergedList.add( new MemberStatementDTO(utilClass.formatDate(balanceBroughtDownDate), "Balance Brought Down",null, balanceBroughtDown));

        while(i >0 && j >0){

            if(claims.get(i-1).getClaimDate().before(premiums.get(j-1).getDate())){

                balance = balance.subtract(claims.get(i-1).getAmount()).setScale(2, RoundingMode.HALF_EVEN);
                System.out.println("claim: " + claims.get(i-1).getAmount());
                mergedList.add(new MemberStatementDTO(utilClass.formatDate(claims.get(i-1).getClaimDate()),"Claim",claims.get(i-1).getAmount(),balance));
                i--;
            }else {
                balance = balance.add(premiums.get(j-1).getAmount()).setScale(2, RoundingMode.HALF_EVEN);
                System.out.println("premium: " + premiums.get(j-1).getAmount());
                mergedList.add(new MemberStatementDTO(utilClass.formatDate(premiums.get(j-1).getDate()),"Contribution",premiums.get(j-1).getAmount(),balance));
                j--;
            }
        }

        while(i> 0){
            balance = balance.subtract(claims.get(i-1).getAmount()).setScale(2, RoundingMode.HALF_EVEN);
            System.out.println("claim after loop: " + claims.get(i-1).getAmount());
            mergedList.add(new MemberStatementDTO(utilClass.formatDate(claims.get(i-1).getClaimDate()),"Claim",claims.get(i-1).getAmount(),balance));
            i--;
        }
        while(j > 0){
            balance = balance.add(premiums.get(j-1).getAmount()).setScale(2, RoundingMode.HALF_EVEN);
            System.out.println("premium after loop: " + premiums.get(j-1).getAmount());
            mergedList.add(new MemberStatementDTO(utilClass.formatDate(premiums.get(j-1).getDate()),"Premium",premiums.get(j-1).getAmount(),balance));
            j--;
        }

        mergedList.add( new MemberStatementDTO(utilClass.formatDate(new Date(System.currentTimeMillis())),"Current Balance",null, balance));

        return mergedList;

    }

    public List getClaimDump (String order){

        List<Claim> claims = new ArrayList<>();

        if(order == null){
            claims = claimRepo.findAll();

        } else if(order.equalsIgnoreCase("buried")){

            claims = claimRepo.findAllByOrderByBuriedDate();

        } else if(order.equalsIgnoreCase("death")){

            claims = claimRepo.findAllByOrderByDeathDate();

        } else if(order.equalsIgnoreCase("claimed")){

            claims = claimRepo.findAllByOrderByClaimDate();

        }

        return claims;

    }

}
