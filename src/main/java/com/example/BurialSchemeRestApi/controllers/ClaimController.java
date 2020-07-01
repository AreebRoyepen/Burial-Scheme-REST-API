package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.api.ErrorMessage;
import com.example.BurialSchemeRestApi.api.ResponseMessageList;
import com.example.BurialSchemeRestApi.dto.ClaimRequestDTO;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.services.ClaimService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/v1/claims")
public class ClaimController {

    public ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping()
    public ResponseEntity<ResponseMessageList> allClaims() {
        return new ResponseEntity<>(claimService.allClaims(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> claim(@RequestBody ClaimRequestDTO claimRequestDTO) {

        try {
            return new ResponseEntity<>(claimService.claim(claimRequestDTO), HttpStatus.OK);
        }catch (ValidationException ex){
            return new ResponseEntity<>(new ErrorMessage(ex.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/dependantClaim")
    public ResponseEntity<?> claimForDep(@RequestBody ClaimRequestDTO claimRequestDTO) {

        try {
            return new ResponseEntity<>(claimService.claimForDep(claimRequestDTO), HttpStatus.OK);
        }catch (ValidationException ex){
            return new ResponseEntity<>(new ErrorMessage(ex.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
        }

    }

}
