package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.api.ErrorMessage;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.services.RelationshipTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("v1/relationships")
public class RelationshipTypeController {


    RelationshipTypeService relationshipTypeService;

    public RelationshipTypeController(RelationshipTypeService relationshipTypeService) {
        this.relationshipTypeService = relationshipTypeService;
    }

    @GetMapping()
    public ResponseEntity<?> relationships() {

        try{
            return new ResponseEntity<>(relationshipTypeService.all(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
        }
    }
}
