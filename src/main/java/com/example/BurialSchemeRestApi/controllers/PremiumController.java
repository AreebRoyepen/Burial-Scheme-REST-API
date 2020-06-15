package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.api.ErrorMessage;
import com.example.BurialSchemeRestApi.dto.PremiumDTO;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.services.PremiumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/v1/premiums")
public class PremiumController {

    PremiumService premiumService;

    public PremiumController(PremiumService premiumService) {
        this.premiumService = premiumService;
    }

    @GetMapping()
    public ResponseEntity<?> allPremiums() {
        return new ResponseEntity<>(premiumService.allPremiums(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> addPremium(@RequestBody PremiumDTO premiumDTO) {

        try {
            return new ResponseEntity<>(premiumService.addPremium(premiumDTO), HttpStatus.OK);
        } catch (ValidationException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
        }

    }


}
