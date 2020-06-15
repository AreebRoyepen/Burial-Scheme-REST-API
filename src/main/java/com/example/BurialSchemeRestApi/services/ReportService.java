package com.example.BurialSchemeRestApi.services;

import com.example.BurialSchemeRestApi.dto.ReportServiceDTO;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.models.Claim;
import com.example.BurialSchemeRestApi.models.Member;
import com.example.BurialSchemeRestApi.reports.ReportData;
import com.example.BurialSchemeRestApi.reports.ReportGenerator;
import com.example.BurialSchemeRestApi.repositories.MemberRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReportService {

    Logger logger = LoggerFactory.getLogger(ReportService.class);

    ReportData reportData;
    MemberRepo memberRepo;
    ReportGenerator reportGenerator;

    public ReportService(ReportData reportData, MemberRepo memberRepo, ReportGenerator reportGenerator) {
        this.reportData = reportData;
        this.memberRepo = memberRepo;
        this.reportGenerator = reportGenerator;
    }

    public ReportServiceDTO getStatement(Long id) throws ValidationException {

        try{
            Member m = memberRepo.findById(id).orElseThrow();

            List mergedList = reportData.getStatementList(m);

            ByteArrayInputStream bis = new ByteArrayInputStream(reportGenerator.generateStatement(m,mergedList));

            HttpHeaders headers = getHttpHeaders( "Statement: " + m.getName() , "pdf");

            return ReportServiceDTO.builder().httpHeaders(headers).contentType(MediaType.APPLICATION_PDF)
                            .inputStream(bis).build();

        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            logger.error("No such Member");
            throw new ValidationException("No such Member");
        }catch(Exception ex){
            ex.printStackTrace();
            throw new ValidationException(ex.getMessage());
        }

    }

    public ReportServiceDTO claimDump(String order) throws ValidationException {

        try{

            List<Claim> claims = reportData.getClaimDump(order);

            // To convert bytes to an InputStream, use a ByteArrayInputStream
            ByteArrayInputStream bis = new ByteArrayInputStream(reportGenerator.generateClaimCSV(claims));

            HttpHeaders headers = getHttpHeaders("deaths", "csv");

            return ReportServiceDTO.builder().httpHeaders(headers).contentType(new MediaType("text", "csv"))
                    .inputStream(bis).build();

        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new ValidationException(e.getMessage());
        }

    }

    public ReportServiceDTO memberDump() throws ValidationException {

        try{
            List<Member> members = memberRepo.findAll();

            // To convert bytes to an InputStream, use a ByteArrayInputStream
            ByteArrayInputStream bis = new ByteArrayInputStream(reportGenerator.generateMemberCSV(members));

            HttpHeaders headers = getHttpHeaders("member info","csv");

            return ReportServiceDTO.builder().httpHeaders(headers).contentType(new MediaType("text", "csv"))
                    .inputStream(bis).build();

        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new ValidationException(e.getMessage());
        }

    }


    private HttpHeaders getHttpHeaders (String reportName, String type){

        Calendar calender = Calendar.getInstance();
        HttpHeaders headers = new HttpHeaders();


        if(type.equalsIgnoreCase("pdf")){

            String filename = reportName + " " + calender.getTime().toString()+".pdf";

            headers.add("Content-Disposition", "inline; "+filename);
            headers.add("Content-Type:", "application/pdf");

        }else if(type.equalsIgnoreCase("csv")){

            String filename = reportName + " " + calender.getTime().toString()+".csv";

            headers.add("Content-Disposition", "inline; "+filename);
            headers.add("Accept:", "text/csv");


        }

        return headers;
    }
}
