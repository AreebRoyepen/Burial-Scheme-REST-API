package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.models.TransactionType;
import com.example.BurialSchemeRestApi.repositories.TransactionTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class UtilController {



    public ResponseEntity<?> responseUtil(String message) {
        Map<String, String> m = new HashMap<String, String>();
        m.put("message", message);
        return ResponseEntity.status(HttpStatus.OK).body(m);
    }

    public void bob (Long id){




    }

}
