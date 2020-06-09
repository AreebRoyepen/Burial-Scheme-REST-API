package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.dto.MemberStatementDTO;
import com.example.BurialSchemeRestApi.models.Claim;
import com.example.BurialSchemeRestApi.models.Member;
import com.example.BurialSchemeRestApi.models.Premium;
import com.example.BurialSchemeRestApi.repositories.ClaimRepo;
import com.example.BurialSchemeRestApi.repositories.MemberRepo;
import com.example.BurialSchemeRestApi.repositories.PremiumRepo;
import com.example.BurialSchemeRestApi.services.ReportService;
import com.example.BurialSchemeRestApi.util.UtilClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
public class ReportController {

    Logger logger = LoggerFactory.getLogger(ReportController.class);
    @Autowired
    ClaimRepo claimRepo;
    @Autowired
    PremiumRepo premiumRepo;
    @Autowired
    MemberRepo memberRepo;
    @Autowired
    UtilClass util;
    @Autowired
    ReportService reportService;
    @Autowired
    UtilClass utilClass;

    @GetMapping("/memberStatement/{id}")
    public ResponseEntity<?> statement(@PathVariable Long id){

        try{
            Member m = memberRepo.findById(id).orElseThrow();
            int year = Calendar.getInstance().get(Calendar.YEAR);

            Date balanceBroughtDownDate = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("31/01/"+year).getTime());

            BigDecimal balanceBroughtDown = utilClass.getBalanceAtDate(m, balanceBroughtDownDate);

            List<Claim> claims = claimRepo.findByMemberAndClaimDateAfter(m,balanceBroughtDownDate);
            List<Premium> premiums = premiumRepo.findByMemberAndDateAfter(m, balanceBroughtDownDate);
            List<MemberStatementDTO> mergedList = new ArrayList<>();

            BigDecimal balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
            int i = claims.size();
            int j = premiums.size();

            mergedList.add( new MemberStatementDTO(balanceBroughtDownDate, "Balance Brought Down",null, balanceBroughtDown));

            while(i >0 && j >0){

                if(claims.get(i-1).getClaimDate().before(premiums.get(j-1).getDate())){

                    balance = balance.subtract(claims.get(i-1).getAmount()).setScale(2, RoundingMode.HALF_EVEN);
                    System.out.println("claim: " + claims.get(i-1).getAmount());
                    mergedList.add(new MemberStatementDTO(claims.get(i-1).getClaimDate(),"Claim",claims.get(i-1).getAmount(),balance));
                    i--;
                }else {
                    balance = balance.add(premiums.get(j-1).getAmount()).setScale(2, RoundingMode.HALF_EVEN);
                    System.out.println("premium: " + premiums.get(j-1).getAmount());
                    mergedList.add(new MemberStatementDTO(premiums.get(j-1).getDate(),"Contribution",premiums.get(j-1).getAmount(),balance));
                    j--;
                }
            }

            while(i> 0){
                balance = balance.subtract(claims.get(i-1).getAmount()).setScale(2, RoundingMode.HALF_EVEN);
                System.out.println("claim after loop: " + claims.get(i-1).getAmount());
                mergedList.add(new MemberStatementDTO(claims.get(i-1).getClaimDate(),"Claim",claims.get(i-1).getAmount(),balance));
                i--;
            }
            while(j > 0){
                balance = balance.add(premiums.get(j-1).getAmount()).setScale(2, RoundingMode.HALF_EVEN);
                System.out.println("premium after loop: " + premiums.get(j-1).getAmount());
                mergedList.add(new MemberStatementDTO(premiums.get(j-1).getDate(),"Premium",premiums.get(j-1).getAmount(),balance));
                j--;
            }

            mergedList.add( new MemberStatementDTO(new Date(System.currentTimeMillis()),"Current Balance",null, balance));

            ByteArrayInputStream bis = new ByteArrayInputStream(reportService.report(m,mergedList));

            Calendar calender = Calendar.getInstance();
            String reportName = "Statement: " + m.getName() + " ";
            String filename = reportName + calender.getTime().toString()+".pdf";

            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-Disposition", "inline; "+filename);
            headers.add("Content-Type:", "application/pdf");

            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));



        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            logger.error("No such Member");
            return util.responseUtil("No such Member");
        }




    }

    @GetMapping("/deaths/{order}")
    public ResponseEntity<?> claimDump(@PathVariable String order){

        List<Claim> claims = new ArrayList<>();
        if(order.equalsIgnoreCase("buried")){

            claims = claimRepo.findAllByOrderByBuriedDate();

        } else if(order.equalsIgnoreCase("death")){

            claims = claimRepo.findAllByOrderByDeathDate();

        } else if(order.equalsIgnoreCase("claimed")){

            claims = claimRepo.findAllByOrderByClaimDate();

        }

        try
        {
            String CSV_SEPARATOR = ",";

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
            for (Claim claim : claims)
            {
                StringBuffer oneLine = new StringBuffer();
                oneLine.append(claim.getBurialPlace());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(claim.getBuriedDate());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(claim.getClaimDate());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(claim.getDeathDate());
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();

            // To convert it to a byte[] - simply use
            final byte[] bytes = stream.toByteArray();

            // To convert bytes to an InputStream, use a ByteArrayInputStream
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

            Calendar calender = Calendar.getInstance();
            String reportName = "deaths" + " ";
            String filename = reportName + calender.getTime().toString()+".csv";

            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-Disposition", "inline; "+filename);
            headers.add("Accept:", "text/csv");

            return ResponseEntity.ok().headers(headers).contentType(new MediaType("text", "csv"))
                    .body(new InputStreamResource(bis));

        }
        catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            logger.error(e.getMessage());
            return util.responseUtil(e.getMessage());
        }
        catch (FileNotFoundException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            logger.error(e.getMessage());
            return util.responseUtil(e.getMessage());
        }
        catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            logger.error(e.getMessage());
            return util.responseUtil(e.getMessage());
        }



    }

    @GetMapping("/memberDump")
    public ResponseEntity<?> memberDump(){

        List<Member> members = memberRepo.findAll();

        try
        {
            String CSV_SEPARATOR = ",";

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
            for (Member member : members)
            {
                StringBuffer oneLine = new StringBuffer();
                oneLine.append(member.getName());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(member.getSurname());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(member.getDOB());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(member.getDOE());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(member.getAddress());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(member.getArea());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(member.getCellNumber());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(member.getHomeNumber());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(member.getWorkNumber());
                oneLine.append(CSV_SEPARATOR);
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();

            // To convert it to a byte[] - simply use
            final byte[] bytes = stream.toByteArray();

            // To convert bytes to an InputStream, use a ByteArrayInputStream
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

            Calendar calender = Calendar.getInstance();
            String reportName = "member info" + " ";
            String filename = reportName + calender.getTime().toString()+".csv";

            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-Disposition", "inline; "+filename);
            headers.add("Accept:", "text/csv");

            return ResponseEntity.ok().headers(headers).contentType(new MediaType("text", "csv"))
                    .body(new InputStreamResource(bis));

        }
        catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            logger.error(e.getMessage());
            return util.responseUtil(e.getMessage());
        }
        catch (FileNotFoundException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            logger.error(e.getMessage());
            return util.responseUtil(e.getMessage());
        }
        catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            logger.error(e.getMessage());
            return util.responseUtil(e.getMessage());
        }

    }

}
