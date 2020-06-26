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

    @GetMapping({"/deaths", "/deaths/{email}"})
    public ResponseEntity<?> claimDump(@PathVariable(required = false) String order,
                                       @PathVariable(required = false) String email){

        try {
            if (email == null){
                ReportServiceDTO m = reportService.claimDump(order);

                return ResponseEntity.ok().headers(m.getHttpHeaders()).contentType(m.getContentType())
                        .body(new InputStreamResource(m.getInputStream()));
            }else{
                return new ResponseEntity<>(reportService.emailClaimDump(order, email), HttpStatus.OK);
            }

        } catch (ValidationException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping({"/memberDump", "/memberDump/{email}"})
    public ResponseEntity<?> memberDump(@PathVariable(required = false) String email){

        try {
            if(email == null){
                ReportServiceDTO m = reportService.memberDump();

                return ResponseEntity.ok().headers(m.getHttpHeaders()).contentType(m.getContentType())
                        .body(new InputStreamResource(m.getInputStream()));

            }else{
                return new ResponseEntity<>(reportService.emailMemberDump(email), HttpStatus.OK);
            }

        } catch (ValidationException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
        }

    }


}
