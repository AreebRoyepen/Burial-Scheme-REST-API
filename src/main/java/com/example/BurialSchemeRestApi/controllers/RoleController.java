package com.example.BurialSchemeRestApi.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BurialSchemeRestApi.repositories.RoleRepo;

@RestController
@CrossOrigin
public class RoleController {

	@Autowired
	RoleRepo roleRepo;
	
	@GetMapping("/roles")
	public ResponseEntity<?> roles() {
		
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("role",roleRepo.findAll());
		
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}
	
}
