package com.example.BurialSchemeRestApi.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.BurialSchemeRestApi.models.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.BurialSchemeRestApi.repositories.PersonRepo;

@RestController
@CrossOrigin
public class PersonController {
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	PersonRepo repo;
	
	@GetMapping("/person")
	public ResponseEntity<?> allPerson() {
		
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("person", repo.findAll());
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}
	
	@PostMapping("/addPerson")
	public ResponseEntity<?> addPerson(@RequestBody Member p){
		
		List <Member> members = repo.findAll();
		Map<String, Object> m = new HashMap<String, Object> ();
		
		for(Member x : members) {
			
			if(	x.getName().equals(p.getName()) && x.getSurname().equals(p.getSurname())) {
				
				if( x.getNumber().equals(p.getNumber()) && x.getEmail().equals(p.getEmail())){
				
					m.put("message", "Person already exists");
					return ResponseEntity.status(HttpStatus.OK).body(m);
				
				}else if(x.getNumber().equals(p.getNumber()) || x.getEmail().equals(p.getEmail())) {
				
					m.put("message", "Similar Person already exists");
					return ResponseEntity.status(HttpStatus.OK).body(m);
				
				}			
			}
			
		}
		
		m.put("message", "success");
		m.put("person", repo.save(p));
		return ResponseEntity.status(HttpStatus.OK).body(m);
	
	}

	
	@GetMapping("/personByName/{name}")
	public ResponseEntity<?> getPersonByName(@PathVariable String name){
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("person", repo.findByName(name));
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}
	
	
	@GetMapping("/personLikeName/{p}")
	public ResponseEntity<?> personLikeName(@PathVariable String p){
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("person",repo.findByNameContains(p));
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}
	
	@GetMapping("/personByID/{id}")
	public ResponseEntity<?> getPersonByID(@PathVariable Long id) {
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("person",repo.findById(id).orElseThrow());
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}

	@DeleteMapping("/deletePerson/{id}")
	public ResponseEntity<?> deletePerson(@PathVariable Long id) {
		try {
			repo.deleteById(id);
			Map<String, String> m = new HashMap<String, String> ();
			m.put("message", "success");
			return ResponseEntity.status(HttpStatus.OK).body(m);
			
		}catch(Exception e) {
			Map<String, String> m = new HashMap<String, String> ();
			m.put("message", "No such Person");
			logger.error("Trying to delete a person that does not exist");
			return ResponseEntity.status(HttpStatus.OK).body(m);
		}
	}
	
	@PutMapping("/updatePerson/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Member p) {
		
		Map<String, Object> m = new HashMap<String, Object> ();
		
		try {
			Member member =repo.findById(id).orElseThrow();

			if (p.getEmail() != null) member.setEmail(p.getEmail());
		  	if (p.getNumber()!= null) member.setNumber(p.getNumber());
		    if (p.getName() != null) member.setName(p.getName());
		    if (p.getSurname() != null) member.setSurname(p.getSurname());
	        
		    m.put("message", "success");
		    m.put("person", repo.save(member));
		    return ResponseEntity.status(HttpStatus.OK).body(m);
			
		}catch(Exception e) {
			
			logger.error("Trying to update a person that does not exist");
			m.put("message", "No such Person");
			return ResponseEntity.status(HttpStatus.OK).body(m);

		}
	}
}
