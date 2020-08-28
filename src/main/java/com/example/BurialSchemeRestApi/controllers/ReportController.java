package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.api.ErrorMessage;
import com.example.BurialSchemeRestApi.dto.ReportServiceDTO;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.services.ReportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/v1/reports")
public class ReportController {

    ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping({"/memberStatement/{id}", "/memberStatement/{id}/{email}"})
    public ResponseEntity<?> statement(@PathVariable Long id, @PathVariable(required = false) String email){

        try {
            if (email == null){
                ReportServiceDTO m = reportService.getStatement(id);
                return ResponseEntity.ok().headers(m.getHttpHeaders()).contentType(m.getContentType())
                        .body(new InputStreamResource(m.getInputStream()));
            }else{
                return new ResponseEntity<>(reportService.emailStatement(id, email), HttpStatus.OK);
            }

        } catch (ValidationException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping({"/claims", "/claims/{email}"})
    public ResponseEntity<?> claimDetails(@PathVariable(required = false) String order,
                                          @PathVariable(required = false) String email){

        try {
            if (email == null){
                ReportServiceDTO m = reportService.claimDetails(order);

                return ResponseEntity.ok().headers(m.getHttpHeaders()).contentType(m.getContentType())
                        .body(new InputStreamResource(m.getInputStream()));
            }else{
                return new ResponseEntity<>(reportService.emailClaimDetails(order, email), HttpStatus.OK);
            }

        } catch (ValidationException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping({"/memberDetails", "/memberDetails/{email}"})
    public ResponseEntity<?> memberDetails(@PathVariable(required = false) String email){

        try {
            if(email == null){
                ReportServiceDTO m = reportService.memberDetails();

                return ResponseEntity.ok().headers(m.getHttpHeaders()).contentType(m.getContentType())
                        .body(new InputStreamResource(m.getInputStream()));

            }else{
                return new ResponseEntity<>(reportService.emailMemberDetails(email), HttpStatus.OK);
            }

        } catch (ValidationException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
        }

    }


}
