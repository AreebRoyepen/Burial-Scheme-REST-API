package com.example.BurialSchemeRestApi.controllers;

import java.util.HashMap;
import java.util.Map;

import com.example.BurialSchemeRestApi.api.ErrorMessage;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BurialSchemeRestApi.repositories.RoleRepo;

@RestController
@CrossOrigin
@RequestMapping("v1/roles")
public class RoleController {

	RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@GetMapping()
	public ResponseEntity<?> roles() {

		try{
			return new ResponseEntity<>(roleService.roles(),HttpStatus.OK);
		}catch (Exception e){
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
		}
	}
	
}
