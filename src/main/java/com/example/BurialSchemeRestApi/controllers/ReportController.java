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

    @GetMapping("/memberStatement/{id}")
    public ResponseEntity<?> statement(@PathVariable Long id){

        try {
            ReportServiceDTO m = reportService.getStatement(id);

            return ResponseEntity.ok().headers(m.getHttpHeaders()).contentType(m.getContentType())
                    .body(new InputStreamResource(m.getInputStream()));

        } catch (ValidationException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping({"/deaths/{order}", "/deaths"})
    public ResponseEntity<?> claimDump(@PathVariable(required = false) String order){

        try {
            ReportServiceDTO m = reportService.claimDump(order);

            return ResponseEntity.ok().headers(m.getHttpHeaders()).contentType(m.getContentType())
                    .body(new InputStreamResource(m.getInputStream()));

        } catch (ValidationException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/memberDump")
    public ResponseEntity<?> memberDump(){

        try {
            ReportServiceDTO m = reportService.memberDump();

            return ResponseEntity.ok().headers(m.getHttpHeaders()).contentType(m.getContentType())
                    .body(new InputStreamResource(m.getInputStream()));

        } catch (ValidationException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
        }

    }

}
