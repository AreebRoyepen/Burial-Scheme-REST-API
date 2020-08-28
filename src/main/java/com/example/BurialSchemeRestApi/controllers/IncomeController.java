package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.api.ErrorMessage;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.services.IncomeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@CrossOrigin
@RequestMapping("/v1/incomes")
public class IncomeController {

    IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping()
    public ResponseEntity<?> allIncome() {

        return new ResponseEntity<>(incomeService.allIncomes(), HttpStatus.OK);

    }

    @GetMapping("/{amount}/{type}")
    public ResponseEntity<?> addIncome(@PathVariable BigDecimal amount, @PathVariable Long type) {

        try {
            return new ResponseEntity<>(incomeService.addIncome(amount, type), HttpStatus.OK);
        } catch (ValidationException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
        }

    }

}
