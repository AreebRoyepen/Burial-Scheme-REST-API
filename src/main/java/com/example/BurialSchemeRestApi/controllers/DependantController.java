package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.models.Dependant;
import com.example.BurialSchemeRestApi.repositories.DependantRepo;
import com.example.BurialSchemeRestApi.util.UtilClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
public class DependantController {
	
	Logger logger = LoggerFactory.getLogger(DependantController.class);
	
	@Autowired
	DependantRepo repo;
	@Autowired
	private UtilClass util;
	
	@GetMapping("/dependants")
	public ResponseEntity<?> allMembers() {
		
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("data", repo.findAll());
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}


	
	@PostMapping("/addDependant")
	public ResponseEntity<?> addDependant(@RequestBody Dependant dependant){
		
		List <Dependant> dependants = repo.findAll();
		Map<String, Object> map = new HashMap<> ();
		
		for(Dependant x : dependants) {
			
			if(	x.getName().equals(dependant.getName()) && x.getSurname().equals(dependant.getSurname())) {
				
				if( x.getDOB().equals(dependant.getDOB())){
					map.put("message", "Similar dependant already exists");
					return ResponseEntity.status(HttpStatus.OK).body(map);
				
				}else if(x.getMember().equals(dependant.getMember())) {
					map.put("message", "Dependant already added");
					return ResponseEntity.status(HttpStatus.OK).body(map);
				
				}
			}
			
		}
		repo.save(dependant);

		map.put("message", "success");
		map.put("data", dependant);
		return ResponseEntity.status(HttpStatus.OK).body(map);
	
	}

	
	@GetMapping("/dependantByName/{name}")
	public ResponseEntity<?> getDependantByName(@PathVariable String name){
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("data", repo.findByName(name));
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}
	
	
	@GetMapping("/dependantLikeName/{p}")
	public ResponseEntity<?> dependantLikeName(@PathVariable String p){
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("data",repo.findByNameContains(p));
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}
	
	@GetMapping("/dependantByID/{id}")
	public ResponseEntity<?> getDependantByID(@PathVariable Long id) {
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("data",repo.findById(id).orElseThrow());
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}

	@DeleteMapping("/deleteDependant/{id}")
	public ResponseEntity<?> deleteDependant(@PathVariable Long id) {
		try {
			Dependant dep = repo.findById(id).orElseThrow();
			repo.deleteById(dep.getID());
			Map<String, String> m = new HashMap<String, String> ();
			m.put("message", "success");
			return ResponseEntity.status(HttpStatus.OK).body(m);
			
		}catch(Exception e) {
			Map<String, String> m = new HashMap<String, String> ();
			m.put("message", "No such Dependant");
			logger.error("Trying to delete a dependant that does not exist");
			return ResponseEntity.status(HttpStatus.OK).body(m);
		}
	}
	
	@PutMapping("/updateDependant/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Dependant dependant) {
		
		Map<String, Object> m = new HashMap<> ();
		
		try {
			Dependant newDependant =repo.findById(id).orElseThrow();

			if(dependant.getDOB() != null) newDependant.setDOB(dependant.getDOB());
			if(dependant.getDOE() != null) newDependant.setDOE(dependant.getDOE());
			if(dependant.getMember() != null) newDependant.setMember(dependant.getMember());
		    if (dependant.getName() != null) newDependant.setName(dependant.getName());
		    if (dependant.getSurname() != null) newDependant.setSurname(dependant.getSurname());
		    if(dependant.getRelationship() != null) newDependant.setRelationship(dependant.getRelationship());
	        
		    m.put("message", "success");
		    m.put("data", repo.save(newDependant));
		    return ResponseEntity.status(HttpStatus.OK).body(m);
			
		}catch(Exception e) {
			
			logger.error("Trying to update a dependant that does not exist");
			m.put("message", "No such dependant");
			return ResponseEntity.status(HttpStatus.OK).body(m);

		}
	}
}
