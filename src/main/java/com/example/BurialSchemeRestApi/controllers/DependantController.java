package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.api.ErrorMessage;
import com.example.BurialSchemeRestApi.dto.DependantDTO;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.services.DependantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/v1/dependants")
public class DependantController {
	
	DependantService dependantService;

	public DependantController(DependantService dependantService) {
		this.dependantService = dependantService;
	}

	@GetMapping()
	public ResponseEntity<?> allMembers() {
		return new ResponseEntity<>(dependantService.allMembers(),HttpStatus.OK);
	}

	@GetMapping("/member/{id}")
	public ResponseEntity<?> myMember(@PathVariable Long id){

		try {
			return new ResponseEntity<>(dependantService.myMember(id), HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping()
	public ResponseEntity<?> addDependant(@RequestBody DependantDTO dependantDTO){

		try {
			return new ResponseEntity<>(dependantService.addDependant(dependantDTO), HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
		}
	
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<?> getDependantByName(@PathVariable String name){
		try {
			return new ResponseEntity<>(dependantService.getDependantByName(name), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping("/search/{p}")
	public ResponseEntity<?> dependantLikeName(@PathVariable String p){
		try {
			return new ResponseEntity<>(dependantService.dependantLikeName(p), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getDependantByID(@PathVariable Long id) {
		try {
			return new ResponseEntity<>(dependantService.getDependantByID(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteDependant(@PathVariable Long id) {
		try {
			return new ResponseEntity<>(dependantService.deleteDependant(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody DependantDTO dependantDTO) {

		try {
			return new ResponseEntity<>(dependantService.update(id,dependantDTO), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
		}

	}
}
